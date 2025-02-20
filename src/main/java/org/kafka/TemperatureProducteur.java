package org.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TemperatureProducteur {
    public static void main(String[] args) throws InterruptedException {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_1_URL);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        final KafkaProducer<String, String> producer = new KafkaProducer(props);
        final String[] buildings = {"Aile A", "Aile B", "Aile C"};
        final String[] salles = {"Salle 1", "Salle 2", "Salle 3"};
        final Random random = new Random();

        int temperature;
        String value;
        ProducerRecord<String, String> record;

        while (true) {
            for (String building : buildings) {
                for (String salle : salles) {
                    temperature = random.nextInt(30);
                    value = salle + " -> " + temperature + "°C";
                    record = new ProducerRecord<>(Constant.PREMIER_TOPIC, building, value);
                    producer.send(record);
                }
            }
            TimeUnit.SECONDS.sleep(5);
        }
    }
}

