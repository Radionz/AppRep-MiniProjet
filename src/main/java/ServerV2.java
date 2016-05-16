
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.NotSerializableException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerV2 {

    private static final Logger logger = LogManager.getLogger(ServerV2.class);
    private static final int PORT = 4000;

    public static void main(String[] args) {
        //Example seller / buyer of vehicles
        List<String> vehicles = new ArrayList<>();
        vehicles.add("Volkswagen Golf");
        vehicles.add("Ford Fiesta");
        vehicles.add("Volkswagen Polo");
        vehicles.add("Renault Clio");
        vehicles.add("Opel/Vauxhall Corsa");
        vehicles.add("Peugeot 208");
        vehicles.add("Ford Focus");
        vehicles.add("Nissan Qashqai");
        vehicles.add("Fiat Panda");
        vehicles.add("Renault Captur");

        List<String> clients = new ArrayList<>();
        clients.add("Jean Paul");
        clients.add("Sparrow");
        clients.add("Rihanna");
        clients.add("Onix");

        logger.trace("Example seller / buyer of vehicles");

        try {
            logger.debug("Server initialization on port " + PORT);
            MyRegistry myRegistry = new MyRegistry(PORT);
            myRegistry.rebind("vehicles", vehicles);
            myRegistry.rebind("clients", clients);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotSerializableException e) {
            e.printStackTrace();
        }

        logger.debug("Server running");
    }
}