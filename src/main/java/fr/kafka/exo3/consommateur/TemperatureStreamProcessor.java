package fr.kafka.exo3.consommateur;

import fr.kafka.Constant;
import fr.kafka.exo3.DoubleArraySerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class TemperatureStreamProcessor {

    private static final double MIN_TEMPERATURE = 10.0;
    private static final double MAX_TEMPERATURE = 25.0;

    public static void main(String[] args) {

        System.out.println("Démarrage du traitement des températures...");

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, Constant.TEMPERATURE_TOPIC);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_2_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream(Constant.TEMPERATURE_TOPIC);

        // Transformation pour récupérer le nom du bâtiment et la température de chaque salle
        KStream<String, Double> roomTemperatureStream = stream.flatMap((building, message) -> {
            try {
                final String[] parts = message.split(";");
                if (parts.length == 2) {
                    final String salle = parts[0].trim();
                    final double temperature = Double.parseDouble(parts[1].trim());
                    return Arrays.asList(KeyValue.pair(building + "_" + salle, temperature));
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du parsing du message : " + message + " (" + e.getMessage() + ")");
            }
            return Arrays.asList();
        });

        // Fenêtre de 5 minutes
        TimeWindows tumblingWindow = TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5));

        // Pour calculer la moyenne de température par salle sous la forme [somme, nombre de valeurs]
        KTable<Windowed<String>, double[]> aggregated = roomTemperatureStream
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .windowedBy(tumblingWindow)
                .aggregate(
                        () -> new double[]{0.0, 0.0},
                        (room, temperature, agg) -> new double[]{agg[0] + temperature, agg[1] + 1},
                        Materialized.with(Serdes.String(), new DoubleArraySerde())
                );

        // Transformation pour calculer la moyenne
        KTable<Windowed<String>, Double> averageTemperature = aggregated.mapValues(agg ->
                agg[1] == 0 ? 0.0 : agg[0] / agg[1]
        );

        // Pour chaque fenêtre -> affichage de la température moyenne par salle
        averageTemperature.toStream().foreach((windowedRoom, avg) -> {
            String room = windowedRoom.key();
            System.out.println(room + " | Température moyenne (fenêtre "
                    + windowedRoom.window().startTime() + " à " + windowedRoom.window().endTime() + ") = " + avg + "°C");
            if (avg < MIN_TEMPERATURE || avg > MAX_TEMPERATURE) {
                System.out.println(Constant.RED + ">>> ALERTE : La température moyenne de la " + room + " est hors seuil (" + avg + "°C) !" + Constant.RESET);
            }
        });

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

