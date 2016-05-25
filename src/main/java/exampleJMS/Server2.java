package exampleJMS;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import registry.IMyRegistry;
import registry.MyRegistry;

import javax.jms.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Dorian on 21/03/2016.
 */
public class Server2 {

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
            textMessage.setText("TEST1");
            messageProducer.send(queue, textMessage);
        } catch (javax.jms.JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        logger.info("JMS Example");

        new Server2().producer(PORT_RMI, PORT_JMS);
    }
}