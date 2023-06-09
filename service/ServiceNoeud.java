package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import raytracer.Image;
import raytracer.Scene;

public interface ServiceNoeud extends Remote {
    String getInformation() throws RemoteException;

    Image donnerImage(Scene scene, int x, int y, int l, int h) throws RemoteException;
    
}
