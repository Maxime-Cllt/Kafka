package fr.kafka;


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


    // Intialisation des topics Kafka
    public static void init() {
        final String topicName[] = {Constant.PREMIER_TOPIC, Constant.TEMPERATURE_TOPIC};
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.KAFKA_1_URL);

        for (String topic : topicName) {
            if (!topicExist(config, topic)) {
                System.out.println("Initiation des topics...");
                createTopic(config, topic, 2, (short) 1);
            }
        }

        listAllTopics(config);
    }


    /**
     * Créer un topic Kafka
     *
     * @param config
     * @param topicName
     * @param numPartitions
     * @param replicationFactor
     */
    public static void createTopic(Properties config, final String topicName, final int numPartitions, final short replicationFactor) {
        try (AdminClient adminClient = AdminClient.create(config)) {
            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            System.out.println("Le topic " + topicName + " a été créé");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lister tous les topics Kafka
     *
     * @param config
     */
    public static void listAllTopics(Properties config) {
        try (AdminClient adminClient = AdminClient.create(config)) {
            ListTopicsResult topics = adminClient.listTopics();
            System.out.println("Liste des Kafka Topics: " + topics.names().get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean topicExist(Properties config, final String topicName) {
        try (AdminClient adminClient = AdminClient.create(config)) {
            ListTopicsResult topics = adminClient.listTopics();
            return topics.names().get().contains(topicName);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}