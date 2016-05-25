package exampleJMS;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import registry.MyRegistry;

import javax.jms.*;

/**
 * Created by Dorian on 21/03/2016.
 */
public class Server2 {

    /**
     * Cette classe server permet de vérifier que la queue JMS fonctionne et que les messages sont bien reçu chez le client
     */

    private static final Logger logger = LogManager.getLogger(Server2.class);
    private static final int PORT_RMI = 4000;
    private static final int PORT_JMS = 61616;

    private void producer(int portRMI, int portJMS) {
        try {
            javax.jms.ConnectionFactory connectionFactory;
            connectionFactory = new ActiveMQConnectionFactory("user", "password", "tcp://localhost:" + portJMS);
            Connection connection = connectionFactory.createConnection("user", "password");

            Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

            Queue queue = null;
            try {
                MyRegistry myRegistry = new MyRegistry(portRMI, portJMS);
                queue = myRegistry.getQueue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            MessageProducer messageProducer = session.createProducer(queue);
            connection.start();

            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("Message de SERVER1");
            messageProducer.send(queue, textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        logger.info("JMS Example");

        new Server2().producer(PORT_RMI, PORT_JMS);
    }
}