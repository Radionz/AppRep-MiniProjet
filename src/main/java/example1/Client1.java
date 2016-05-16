package example1;

import registry.IMyRegistry;
import registry.MyRegistry;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Dorian on 21/03/2016.
 */
public class Client1 {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 4000);
            IMyRegistry myRegistry = (IMyRegistry) registry.lookup("registry");
            System.out.println(myRegistry.lookup("vehicles"));
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}