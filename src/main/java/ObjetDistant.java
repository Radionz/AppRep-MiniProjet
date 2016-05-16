import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;


/**
 * Created by Dorian on 21/03/2016.
 */
public class ObjetDistant extends java.rmi.server.UnicastRemoteObject implements Distante {

    private static final Logger logger = LogManager.getLogger(ObjetDistant.class);

    protected ObjetDistant() throws RemoteException {
        super();
    }

    public void echo() throws RemoteException {
        logger.trace("Invocation de la méthode echo()");
    }

    public String sayHello() throws RemoteException {
        logger.trace("Invocation de la méthode sayHello()");
        return "Hello bro !";
    }
}
