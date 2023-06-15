import noeud.Noeud;
import service.ServiceDistributeur;
import service.ServiceNoeud;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainNoeud {

    /**
     *
     * @param args [IP-Distributeur] [port-Distributeur]
     */
    public static void main(String[] args) throws RemoteException, NotBoundException {

        String host = "localhost";
        int port = 1099;

        if(args.length > 0){
            host = args[0];
            if(args.length > 1){
                port = Integer.parseInt(args[1]);
            }
        }

        Noeud di = new Noeud();
        System.out.println("IP address: "+di.getInformation());
        ServiceNoeud si = (ServiceNoeud) UnicastRemoteObject.exportObject(di, 0);
        Registry reg = LocateRegistry.getRegistry(host,port);
        ServiceDistributeur sd = (ServiceDistributeur) reg.lookup("distributeur");
        sd.enregistrerNoeud(si);
        System.out.println("Service enregistre");

    }

}
