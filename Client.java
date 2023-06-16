import raytracer.Disp;
import raytracer.Image;
import raytracer.Scene;
import service.ServiceDistributeur;
import service.ServiceNoeud;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Client {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [IP-Distributeur] [port-Distributeur] [fichier-scène] [largeur] [hauteur]\n\tIP-Distributeur : l'adresse IP de Distributeur (par défaut localhost)\n\tport-Distributeur : la port de Distributeur (par défaut 1099)\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";

    /**
     * @param largeur        largeur de l'image
     * @param hauteur        hauteur de l'image
     * @param desiredLargeur largeur de morceau
     * @param desiredHauteur hauteur de morceau
     * @return Exemple {[0,0,10,10],[10,10,10,10],[20,20,10,10]...}
     */
    public static ArrayList<int[]> decoupeEnList(int largeur, int hauteur, int desiredLargeur, int desiredHauteur) {

        ArrayList<int[]> imageList = new ArrayList<>();

        for (int x = 0; x < largeur; x += desiredLargeur) {
            for (int y = 0; y < hauteur; y += desiredHauteur) {
                int l = Math.min(desiredLargeur, largeur - x);
                int h = Math.min(desiredHauteur, hauteur - y);
                imageList.add(new int[]{x, y, l, h});
            }
        }

//        for (int[] crop : imageList) {
//            int x = crop[0];
//            int y = crop[1];
//            int l = crop[2];
//            int h = crop[3];
//            System.out.println("(" + x + ", " + y + ", " + l + ", " + h + ")");
//        }
        return imageList;
    }
}
