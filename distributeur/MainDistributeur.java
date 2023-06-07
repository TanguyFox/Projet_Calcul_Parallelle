package distributeur;

import service.ServiceDistributeur;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainDistributeur {

    public static void main(String[] args) throws RemoteException {

        int port = 1099;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }

        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ip = localHost != null ? localHost.getHostAddress() : "Unknown IP";
        System.out.println("IP address:"+ip);

        DistributeurNoeud dn = new DistributeurNoeud();
        ServiceDistributeur sd = (ServiceDistributeur) UnicastRemoteObject.exportObject(dn, 0);

        Registry reg = LocateRegistry.createRegistry(port);
        reg.rebind("distributeur", sd);

        System.out.println("EN attente d'une requete");


    }
}
