package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur extends Remote {

    public void enregistrerNoeud(ServiceNoeud noeud) throws RemoteException;

    public ServiceNoeud donnerNoeud() throws RemoteException;
}