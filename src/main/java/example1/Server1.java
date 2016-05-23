package example1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import registry.MyRegistry;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.NotSerializableException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Server1 {

    private static final Logger logger = LogManager.getLogger(Server1.class);
    private static final int PORT_RMI = 4000;
    private static final int PORT_JMS = 4001;

    private javax.jms.Connection connection = null;
    private javax.jms.Session sendSession = null;
    private javax.jms.MessageProducer sender = null;
    InitialContext initialContext = null;

    public static void main(String[] args) {

        startRMIServer(PORT_RMI);
        try {
            new Server1().configurer(PORT_JMS);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private static void startRMIServer(int port) {
        //Example seller / buyer of vehicles
        List<String> vehicles = new ArrayList<>();
        vehicles.add("Volkswagen Golf");
        vehicles.add("Ford Fiesta");
        vehicles.add("Volkswagen Polo");
        vehicles.add("Renault Clio");
        vehicles.add("Opel/Vauxhall Corsa");
        vehicles.add("Peugeot 208");
        vehicles.add("Ford Focus");
        vehicles.add("Nissan Qashqai");
        vehicles.add("Fiat Panda");
        vehicles.add("Renault Captur");

        List<String> clients = new ArrayList<>();
        clients.add("Jean Paul");
        clients.add("Sparrow");
        clients.add("Rihanna");
        clients.add("Onix");

        logger.info("Example seller / buyer of vehicles");
        logger.info("Seller side, gives the list of vehicles for sale");
        try {
            logger.trace("RMI Server initialization on port " + port);
            MyRegistry myRegistry = new MyRegistry(port);
            myRegistry.rebind("vehicles", vehicles);
            myRegistry.rebind("clients", clients);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotSerializableException e) {
            e.printStackTrace();
        }

        logger.trace("RMI Server running ...");
    }

    private void configurer(int port) throws JMSException {
        try {
            logger.trace("Create a connection on port " + port);
            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.put(Context.PROVIDER_URL, "tcp://localhost:" + port);

            initialContext = new InitialContext(properties);

            javax.jms.ConnectionFactory factory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = factory.createConnection();
            this.configurerPublisher();

            logger.trace("Connection activation ...");
            connection.start();
        } catch (javax.jms.JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        MapMessage mapMessage = sendSession.createMapMessage();
        mapMessage.setString("TEST","1");
        sender.send(mapMessage);
    }

    private void configurerPublisher() throws JMSException, NamingException {
        sendSession = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
        Topic topic = (Topic) initialContext.lookup("jms");
        sender = sendSession.createProducer(topic);
    }
}