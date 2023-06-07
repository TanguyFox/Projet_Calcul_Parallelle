package distributeur;

import service.ServiceDistributeur;
import service.ServiceImage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
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
        String clientIP = null;
        try {
            clientIP = RemoteServer.getClientHost();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
        System.out.println("Noeud Ajouté - IP: " + clientIP);
    }

    public synchronized ServiceImage donnerNoeud() throws RemoteException {
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


}