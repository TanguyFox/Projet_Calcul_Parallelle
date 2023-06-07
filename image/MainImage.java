package image;

import service.ServiceDistributeur;
import service.ServiceImage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainImage {

    public static void main(String[] args) throws RemoteException, NotBoundException {

        String host = "localhost";
        int port = 1099;

        if(args.length > 0){
            host = args[0];
            if(args.length > 1){
                port = Integer.parseInt(args[1]);
            }
        }


        DistributeurImage di = new DistributeurImage();
        System.out.println("IP address: "+di.getInformation());

        ServiceImage si = (ServiceImage) UnicastRemoteObject.exportObject(di, 0);

        Registry reg = LocateRegistry.getRegistry(host,port);

        ServiceDistributeur sd = (ServiceDistributeur) reg.lookup("distributeur");
        sd.enregistrerNoeud(si);

        System.out.println("Service enregistre");

    }

}
