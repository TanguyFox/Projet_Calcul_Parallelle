package distributeur;

import service.ServiceDistributeur;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainDistributeur {

    public static void main(String[] args) throws RemoteException {


        DistributeurNoeud dn = new DistributeurNoeud();
        ServiceDistributeur sd = (ServiceDistributeur) UnicastRemoteObject.exportObject(dn, 0);

        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("distributeur", sd);

        System.out.println("EN attente d'une requete");


    }
}
