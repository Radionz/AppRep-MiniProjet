package example1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import registry.MyRegistry;

import java.io.NotSerializableException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Server1 {

    /**
     * Cette classe client permet de creer un registre MyRegistry
     * de le bind dans le registre RMI
     * et ajoute des infos
     * enregistrée dans le server pour démontrer le fonctionnement de celui ci.
     */

    private static final Logger logger = LogManager.getLogger(Server1.class);
    private static final int PORT_RMI = 4000;
    private static final int PORT_JMS = 61616;

    public static void main(String[] args) {
        startRMIServer(PORT_RMI, PORT_JMS);
    }

    private static void startRMIServer(int portRMI, int portJMS) {
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

        logger.info("Example seller / buyer of vehicles");
        logger.info("Seller side, gives the list of vehicles for sale");
        try {
            MyRegistry myRegistry = new MyRegistry(portRMI, portJMS);
            myRegistry.rebind("vehicles", vehicles);
            myRegistry.rebind("clients", clients);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotSerializableException e) {
            e.printStackTrace();
        }
    }
}