package distributeur;

import noeud.NoeudInfo;
import service.ServiceDistributeur;
import service.ServiceNoeud;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.*;

public class DistributeurNoeud implements ServiceDistributeur {

    private HashMap<String,ServiceNoeud> noeuds;
    public void supprimerNoeud(String ip){
        noeuds.remove(ip);
    }

    public DistributeurNoeud() {
        this.noeuds = new HashMap<String, ServiceNoeud>() {};
    }

    public void enregistrerNoeud(ServiceNoeud noeud) throws RemoteException {
        this.noeuds.put(noeud.getInformation(),noeud);
        String clientIP = null;
        try {
            clientIP = RemoteServer.getClientHost();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
        System.out.println("Noeud Ajouté - IP: " + clientIP);
    }

    public synchronized NoeudInfo donnerNoeud() throws RemoteException {
        List<String> keys = List.copyOf(noeuds.keySet());
        int index = new Random().nextInt(noeuds.size());
        return new NoeudInfo(keys.get(index),noeuds.get(keys.get(index)));
    }

}