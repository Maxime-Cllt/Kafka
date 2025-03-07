package fr.kafka.exo3.producteur;

import fr.kafka.Constant;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TemperatureProducteur {


    private final String nomBatiment;
    private final TemperatureSalle[] salles;


    public TemperatureProducteur(final String nomBatiment) {
        this.nomBatiment = nomBatiment;
        final Random random = new Random();
        final int rd = random.nextInt(5) + 1;
        TemperatureSalle[] salles = new TemperatureSalle[rd];
        for (int i = 0; i < rd; i++) {
            salles[i] = new TemperatureSalle("Salle " + i);
            salles[i].start();
        }
        this.salles = salles;
    }

    public void sendTemperature() {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_1_URL);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        final KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        final Random random = new Random();

        String value;
        ProducerRecord<String, String> record;

        while (true) {
            for (TemperatureSalle salle : this.salles) {
                value = salle.name() + ";" + random.nextInt(35);
                record = new ProducerRecord<>(Constant.TEMPERATURE_TOPIC, nomBatiment, value);
                producer.send(record);
                System.out.println("Envoi de " + value + " pour le bâtiment " + nomBatiment);
            }
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

