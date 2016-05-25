package exampleJMS;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import registry.IMyRegistry;

import javax.jms.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client2 implements javax.jms.MessageListener {

    private static final Logger logger = LogManager.getLogger(Client2.class);
    private static final int PORT_RMI = 4000;
    private static final int PORT_JMS = 61616;

    private void consumer(int portRMI, int portJMS) {
        try {
            javax.jms.ConnectionFactory connectionFactory;
            connectionFactory = new ActiveMQConnectionFactory("user", "password", "tcp://localhost:" + portJMS);
            Connection connection = connectionFactory.createConnection("user", "password");

            Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            Queue queue = null;
            try {
                Registry registry = LocateRegistry.getRegistry("localhost", portRMI);
                IMyRegistry myRegistry = (IMyRegistry) registry.lookup("registry");
                queue = myRegistry.getQueue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            javax.jms.MessageConsumer messageConsumer = session.createConsumer(queue);
            messageConsumer.setMessageListener(this);
            connection.start();
        } catch (javax.jms.JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            logger.debug(((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startRMIServer(PORT_RMI, PORT_JMS);
    }

    private static void startRMIServer(int portRMI, int portJMS) {
        logger.info("JMS Example");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        new Client2().consumer(portRMI, portJMS);
    }
}