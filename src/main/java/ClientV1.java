import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Dorian on 21/03/2016.
 */
public class ClientV1 {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 4000);
            Distante distante = (Distante) registry.lookup("objetDistant");
            System.out.println(distante.sayHello());
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}