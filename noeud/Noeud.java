package noeud;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import raytracer.Image;
import raytracer.Scene;
import service.ServiceNoeud;

public class Noeud implements ServiceNoeud {
    private String ip;

    public Noeud(){
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
