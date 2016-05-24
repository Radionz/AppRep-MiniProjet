package registry;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by blanc on 16/05/2016.
 */
public class MyRegistry extends UnicastRemoteObject implements IMyRegistry, Serializable {

    private Hashtable<String, Object> registryTable;
    private List<Event> events;
    private Registry registry;
    private int timestamp;

    public MyRegistry(int port) throws RemoteException {
        super();

        registryTable = new Hashtable<>();
        events = new ArrayList<>();
        timestamp = 0;

       /* if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }*/

        try {
            registry = LocateRegistry.createRegistry(port);
            registry.rebind("registry", this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void rebind(String key, Object object) throws RemoteException, NotSerializableException {
        if (object instanceof Serializable) {
            registryTable.put(key, object);
            if (!events.contains(key))
                events.add(new Event(key, timestamp));
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

    public List<String> mostRequestedKeys(int quantity) throws RemoteException {
        List<String> strings = new ArrayList<>();
        events.sort((Event e1, Event e2) -> new Integer(e2.getRequestNb()).compareTo(e1.getRequestNb()));
        if(quantity > events.size())
            quantity = events.size();
        for(Event event : events.subList(0,quantity))
            strings.add(event.getKey());

        return strings;
    }

    public List<String> mostRequestedKeys(int quantity, int interval) throws RemoteException {
        List<String> strings = new ArrayList<>();
        events.sort((Event e1, Event e2) -> new Integer(e2.getRequestNb()).compareTo(e1.getRequestNb()));
        if(quantity > events.size())
            quantity = events.size();
        for(Event event : events.subList(0,quantity))
            if(event.getTimestamp() > interval ) strings.add(event.getKey());

        return strings;
    }
}
