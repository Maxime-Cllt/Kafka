package org.kafka.exo2;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.kafka.Constant;

import java.util.Properties;
import java.util.UUID;

public class Producer {
    public static void main(String[] args) {


        // Configuration du producteur
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_1_URL);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        try (org.apache.kafka.clients.producer.Producer<String, String> producer = new KafkaProducer<>(props)) {

            int i = 0;
            String message;
            final long now = System.currentTimeMillis();
            ProducerRecord<String, String> record;

            while (true) {

                message = UUID.randomUUID().toString();
                record = new ProducerRecord<>(Constant.PREMIER_TOPIC, message);
                producer.send(record);

                i++;
                if (i > 1_000_000) {
                    break;
                }

            }

            System.out.println("Temps d'exécution : " + (System.currentTimeMillis() - now) + " ms");

        }
    }
}
