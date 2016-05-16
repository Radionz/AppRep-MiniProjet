import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerV1 {

    static ObjetDistant objetDistant;

    public static void main(String[] args) {
        try {
            objetDistant = new ObjetDistant();
            Registry registry = LocateRegistry.createRegistry(4000);
            registry.rebind("objetDistant", objetDistant);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}