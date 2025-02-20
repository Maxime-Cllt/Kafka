package org.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Scanner;

public class Producer {
    public static void main(String[] args) {

        // Configuration du producteur
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        try (org.apache.kafka.clients.producer.Producer<String, String> producer = new KafkaProducer<>(props)) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Entrez un message à envoyer (ou 'exit' pour quitter) :");

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) break;

                ProducerRecord<String, String> record = new ProducerRecord<>("mon-topic", message);
                producer.send(record);
                System.out.println("Message envoyé : " + message);
            }
        }
    }
}
