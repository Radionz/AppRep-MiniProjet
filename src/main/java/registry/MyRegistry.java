package registry;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by blanc on 16/05/2016.
 */
public class MyRegistry extends UnicastRemoteObject implements IMyRegistry, Serializable {

    private static final Logger logger = LogManager.getLogger(MyRegistry.class);

    private Hashtable<String, Object> registryTable;
    private List<Event> events;
    private Registry registry;
    private int timestamp;

    private Queue queue;
    private Session session;
    private MessageProducer messageProducer;

    public javax.jms.Queue getQueue() {
        return queue;
    }

    public MyRegistry(int portRMI, int portJMS) throws RemoteException {
        super();

        registryTable = new Hashtable<>();
        events = new ArrayList<>();
        timestamp = 0;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        logger.trace("RMI Server initialization on port " + portRMI);

        try {
            registry = LocateRegistry.createRegistry(portRMI);
            registry.rebind("registry", this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        logger.trace("RMI Server running ...");

        logger.trace("JMS Queue on port " + portJMS);
        createJMSQueue(portJMS);
    }

    public void rebind(String key, Object object) throws RemoteException, NotSerializableException {
        if (object instanceof Serializable) {
            registryTable.put(key, object);
            if (!events.contains(key)) {
                events.add(new Event(key, timestamp));
                sendNotification("New object added in the registry. Key: "+key);
            }
            timestamp++;
        } else throw new NotSerializableException();
    }

    public Object lookup(String key) throws RemoteException, NotBoundException {
        if (registryTable.containsKey(key)) {
            for (Event event : events)
                if (event.getKey().equals(key))
                    event.wasRequested(timestamp);
            return registryTable.get(key);
        } else throw new NotBoundException();
    }

    public int getLastEventNumber() throws RemoteException {
        return this.timestamp;
    }

    public List<Object> getLast(int quantity) throws RemoteException {
        List<Object> objects = new ArrayList<>();
        for (Event event : events)
            if (event.getTimestamp() >= (timestamp - quantity))
                objects.add(registryTable.get(event.getKey()));
        return objects;
    }

    public List<String> lastKeys(int quantity) throws RemoteException {
        List<String> strings = new ArrayList<>();
        for (Event event : events)
            if (event.getTimestamp() >= (timestamp - quantity))
                strings.add(event.getKey());
        return strings;
    }

    public void createJMSQueue(int port) {
        javax.jms.ConnectionFactory connectionFactory;
        connectionFactory = new ActiveMQConnectionFactory("jms-blanc-pavone-login", "jms-blanc-pavone-mdp", "tcp://localhost:" + port);

        javax.jms.Connection connection;
        try {
            connection = connectionFactory.createConnection("jms-blanc-pavone-login", "jms-blanc-pavone-mdp");
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            this.queue = session.createQueue("jms_queue");
            messageProducer = session.createProducer(queue);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(String message) {
        try {
            for (int i = 0; i < 5; i++) {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(message);
                messageProducer.send(textMessage);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public List<String> mostRequestedKeys(int quantity) throws RemoteException {
        List<String> strings = new ArrayList<>();

        events.sort((Event e1, Event e2) -> new Integer(e2.getRequestNb()).compareTo(e1.getRequestNb()));
        if (quantity > events.size())
            quantity = events.size();
        strings.addAll(events.subList(0, quantity).stream().map(event -> event.getKey()).collect(Collectors.toList()));
        return strings;
    }

    public List<String> mostRequestedKeys(int quantity, int interval) throws RemoteException {
        List<String> strings = new ArrayList<>();
        events.sort((Event e1, Event e2) -> new Integer(e2.getRequestNb()).compareTo(e1.getRequestNb()));
        if (quantity > events.size())
            quantity = events.size();
        strings.addAll(events.subList(0, quantity).stream().filter(event -> event.getTimestamp() > interval).map(event -> event.getKey()).collect(Collectors.toList()));
        return strings;
    }
}
