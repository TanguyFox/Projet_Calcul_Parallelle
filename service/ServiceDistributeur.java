package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur extends Remote {

    public void enregistrerNoeud(ServiceImage noeud) throws RemoteException;

    public ServiceImage donnerNoeud() throws RemoteException;
}