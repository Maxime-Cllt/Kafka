package fr.kafka.exo2;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMS {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "test.queue";

    public static void main(String[] args) {
        try {
            final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

            final Connection connection = connectionFactory.createConnection();
            connection.start();

            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination destination = session.createQueue(QUEUE_NAME);

            final MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            final String message = "Kafka est mieux que JMS";
            final TextMessage messageText = session.createTextMessage(message);

            producer.send(messageText);
            System.out.println("Message envoyé : " + message);

            final MessageConsumer consumer = session.createConsumer(destination);

            Message receivedMessage = consumer.receive(1000);
            if (receivedMessage instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) receivedMessage;
                System.out.println("Message reçu : " + textMessage.getText());
            } else {
                System.out.println("Aucun messageText texte reçu.");
            }

            // Fermeture des ressources
            consumer.close();
            session.close();
            connection.close();

        } catch (Exception e) {
            System.err.println("Exception lors de l'exécution de JMS : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
