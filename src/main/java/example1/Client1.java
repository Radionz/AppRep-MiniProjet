package example1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import registry.IMyRegistry;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Hashtable;

/**
 * Created by Dorian on 21/03/2016.
 */
public class Client1{

    private static final Logger logger = LogManager.getLogger(Server1.class);
    private static final int PORT_RMI = 4000;
    private static final int PORT_JMS = 4001;

    public static void main(String[] args) {
        //Example seller / buyer of vehicles
        logger.info("Example seller / buyer of vehicles");
        logger.info("Buyer side, retrieves the list of vehicles for sale");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            logger.trace("Trying to get the registry bound on port " + PORT_RMI);
            Registry registry = LocateRegistry.getRegistry("localhost", PORT_RMI);
            IMyRegistry myRegistry = (IMyRegistry) registry.lookup("registry");


            logger.debug("List of vehicles " + myRegistry.lookup("vehicles"));
            logger.debug("List of clients " + myRegistry.lookup("clients"));
            logger.debug("getLast " + myRegistry.getLast(5));
            logger.debug("getLastEventNumber " + myRegistry.getLastEventNumber());
            logger.debug("lastKeys " + myRegistry.lastKeys(5));
            logger.debug("mostRequestedKeys " + myRegistry.mostRequestedKeys(5));

        } catch (RemoteException e1) {
            e1.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}