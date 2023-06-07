import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import raytracer.Image;
import raytracer.Scene;
import service.ServiceDistributeur;
import service.ServiceImage;

public class DistributeurImage implements ServiceImage {

    @Override
    public Image donnerImage(Scene scene, int x, int y, int l, int h) throws RemoteException {

       return scene.compute(x, y, l, h);
        
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {

        DistributeurImage di = new DistributeurImage();
        ServiceImage si = (ServiceImage) UnicastRemoteObject.exportObject(di, 0);

        Registry reg = LocateRegistry.getRegistry(1099);
        ServiceDistributeur sd = (ServiceDistributeur) reg.lookup("distributeur");
        sd.enregistrerNoeud(si);

        System.out.println("Service enregistre");

    }
}
