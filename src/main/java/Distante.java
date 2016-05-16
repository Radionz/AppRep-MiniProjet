/**
 * Created by Dorian on 21/03/2016.
 */
public interface Distante extends java.rmi.Remote {

    void echo() throws java.rmi.RemoteException;

    String sayHello() throws java.rmi.RemoteException;
}

