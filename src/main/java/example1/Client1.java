package example1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import registry.IMyRegistry;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Dorian on 21/03/2016.
 */
public class Client1 {

    private static final Logger logger = LogManager.getLogger(Server1.class);
    private static final int PORT = 4000;

    public static void main(String[] args) {
        //Example seller / buyer of vehicles
        logger.info("Example seller / buyer of vehicles");
        logger.info("Buyer side, retrieves the list of vehicles for sale");

        try {
            logger.trace("Trying to get the registry bound on port " + PORT);
            Registry registry = LocateRegistry.getRegistry("localhost", PORT);
            IMyRegistry myRegistry = (IMyRegistry) registry.lookup("registry");


            logger.debug("List of vehicles " + myRegistry.lookup("vehicles"));
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