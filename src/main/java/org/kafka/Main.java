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
        Main.init();
    }


    public static void init() {

        System.out.println("Initiation des topics...");

        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_1_URL);

        try (AdminClient adminClient = AdminClient.create(config)) {

            final String topicName = "premier";
            final int numPartitions = 3;
            final short replicationFactor = 2;

            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);

            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            System.out.println("Le topic " + topicName + " a été créé");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        try (AdminClient adminClient = AdminClient.create(config)) {
            ListTopicsResult topics = adminClient.listTopics();
            System.out.println("Liste des Kafka Topics: " + topics.names().get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}