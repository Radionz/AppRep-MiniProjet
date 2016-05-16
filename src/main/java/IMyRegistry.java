import java.io.NotSerializableException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by blanc on 16/05/2016.
 */
public interface IMyRegistry extends java.rmi.Remote {

    void rebind(String key, Object object) throws RemoteException, NotSerializableException;

    Object lookup(String key) throws RemoteException, NotBoundException;

    int getLastEventNumber() throws RemoteException;

    List<Object> getLast(int quantity) throws RemoteException;

    List<String> lastKeys(int quantity) throws RemoteException;

    List<String> mostRequestedKeys(int quantity) throws RemoteException;
}
