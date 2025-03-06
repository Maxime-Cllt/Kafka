package fr.kafka.exo3.consommateur;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import fr.kafka.Constant;
import fr.kafka.exo3.DoubleArraySerde;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class TemperatureStreamProcessor {

    private static final double MIN_TEMPERATURE = 5.0;
    private static final double MAX_TEMPERATURE = 30.0;

    public static void main(String[] args) {

        System.out.println("Démarrage du traitement des températures...");

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, Constant.TEMPERATURE_TOPIC);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_2_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> stream = builder.stream(Constant.TEMPERATURE_TOPIC);


        KStream<String, Double> roomTemperatureStream = stream.flatMap((building, message) -> {
            try {
                final String[] parts = message.split(";");
                if (parts.length == 2) {
                    final String salle = parts[0].trim();
                    final double temperature = Double.parseDouble(parts[1].trim());
                    return Arrays.asList(KeyValue.pair(salle, temperature));
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du parsing du message : " + message + " (" + e.getMessage() + ")");
            }
            return Arrays.asList();
        });

        // Fenêtre tumblante de 5 minutes (sans délai de grâce)
        TimeWindows tumblingWindow = TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5));

        // Pour calculer la moyenne, nous allons agréger sous la forme d'un tableau [somme, nombre]
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

        // Pour chaque calcul, on vérifie si la moyenne sort des seuils et on émet une alerte
        averageTemperature.toStream().foreach((windowedRoom, avg) -> {
            String room = windowedRoom.key();
            System.out.println("Salle : " + room + " | Température moyenne (fenêtre "
                    + windowedRoom.window().startTime() + " à " + windowedRoom.window().endTime() + ") = " + avg);
            if (avg < MIN_TEMPERATURE || avg > MAX_TEMPERATURE) {
                System.out.println(">>> ALERTE : La température moyenne de la " + room + " est hors seuil (" + avg + "°C) !");
            }
        });

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        // Ajout d'un hook d'arrêt propre
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

