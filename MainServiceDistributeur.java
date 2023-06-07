import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainServiceDistributeur {


    public static void main(String[] args) throws RemoteException {
        
        DistributeurImage di = new DistributeurImage();
        ServiceImage si = (ServiceImage) UnicastRemoteObject.exportObject(di, 0);

        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("image", si);

        System.out.println("EN attente d'une requete");

        


    }
    
}
