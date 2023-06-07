import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur {

    public void enregistrerNoeud(ServiceDistributeur noeud) throws RemoteException;

    public ServiceImage donnerNoeud() throws RemoteException;
}