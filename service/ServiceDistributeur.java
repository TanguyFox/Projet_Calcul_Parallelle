package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur {

    public void enregistrerNoeud(ServiceImage noeud) throws RemoteException;

    public void enregistrerNoeud(ServiceDistributeur noeud) throws RemoteException;

    public ServiceImage donnerNoeud() throws RemoteException;
}