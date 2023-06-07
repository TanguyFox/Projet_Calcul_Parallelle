package image;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private String ip;

    public DistributeurImage(){
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.ip = localHost != null ? localHost.getHostAddress() : "Unknown IP";
    }

    public String getInformation(){
        return ip;
    }

    @Override
    public Image donnerImage(Scene scene, int x, int y, int l, int h) throws RemoteException {
        System.out.println("Calcul... x:"+x+" - y:"+y+" - largeur:"+l+" - hauteur:"+h);
        return scene.compute(x, y, l, h);
    }


}
