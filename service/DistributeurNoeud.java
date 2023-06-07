package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class DistributeurNoeud implements ServiceDistributeur {

    private List<ServiceImage> noeuds = new ArrayList<>();
    private int index;
    public DistributeurNoeud() {
        this.index = 0;
    }

    public void enregistrerNoeud(ServiceImage noeud) throws RemoteException {
        this.noeuds.add(noeud);
    }

    public ServiceImage donnerNoeud() throws RemoteException {
        if (index < noeuds.size()) {
            ServiceImage n = noeuds.get(index);
            index++;
            return n;
        } else {
            index = 0;
            ServiceImage n = noeuds.get(index);
            index++;
            return n;
        }
    }

    public static void main(String[] args) throws RemoteException {

        DistributeurNoeud dn = new DistributeurNoeud();
        ServiceDistributeur sd = (ServiceDistributeur) UnicastRemoteObject.exportObject(dn, 0);

        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("distributeur", sd);

        System.out.println("EN attente d'une requete");

    }
}