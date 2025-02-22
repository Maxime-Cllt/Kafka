package org.kafka.exo3.producteur;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.kafka.Constant;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TemperatureProducteur {


    private String nomBatiment;
    private String[] salles;


    public TemperatureProducteur(final String nomBatiment) {
        this.nomBatiment = nomBatiment;
        final Random random = new Random();
        final int rd = random.nextInt(3) + 1;
        String[] salles = new String[rd];
        for (int i = 0; i < rd; i++) {
            salles[i] = "Salle " + (random.nextInt(10) + 1);
        }
        this.salles = salles;
    }

    public void sendTemperature() {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_1_URL);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        final KafkaProducer<String, String> producer = new KafkaProducer(props);
        final Random random = new Random();

        String value;
        ProducerRecord<String, String> record;

        while (true) {
            for (String salle : this.salles) {
                value = salle + ";" + random.nextInt(30);
                record = new ProducerRecord<>(Constant.TEMPERATURE_TOPIC, nomBatiment, value);
                producer.send(record);
                System.out.println("Envoi de " + value);
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

