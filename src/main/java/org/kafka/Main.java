package org.kafka;


import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {

        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(config)) {

            String topicName = "rahman-topic";
            int numPartitions = 3;
            short replicationFactor = 1; // Attention : 1 seul broker = 1 réplique

            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);

            // Création du topic
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            System.out.println("Topic '" + topicName + "' créé avec succès !");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        try (AdminClient adminClient = AdminClient.create(config)) {
            ListTopicsResult topics = adminClient.listTopics();
            System.out.println("Kafka Topics: " + topics.names().get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Hello World");


    }
}