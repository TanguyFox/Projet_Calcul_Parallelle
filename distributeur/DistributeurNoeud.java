package distributeur;

import service.ServiceDistributeur;
import service.ServiceNoeud;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.List;

public class DistributeurNoeud implements ServiceDistributeur {

    private List<ServiceNoeud> noeuds = new ArrayList<>();
    private int index;

    public DistributeurNoeud() {
        this.index = 0;
    }

    public void enregistrerNoeud(ServiceNoeud noeud) throws RemoteException {
        this.noeuds.add(noeud);
        String clientIP = null;
        try {
            clientIP = RemoteServer.getClientHost();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
        System.out.println("Noeud Ajouté - IP: " + clientIP);
    }

    public synchronized ServiceNoeud donnerNoeud() throws RemoteException {
        if (index < noeuds.size()) {
            ServiceNoeud n = noeuds.get(index);
            index++;
            return n;
        } else {
            index = 0;
            ServiceNoeud n = noeuds.get(index);
            index++;
            return n;
        }
    }


}