package service;

import noeud.NoeudInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur extends Remote {

    void enregistrerNoeud(ServiceNoeud noeud) throws RemoteException;

    NoeudInfo donnerNoeud() throws RemoteException;

    void supprimerNoeud(String ip) throws RemoteException;

}