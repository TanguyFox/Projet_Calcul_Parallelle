import noeud.NoeudInfo;
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
import java.util.concurrent.atomic.AtomicInteger;

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

    /***
     *
     * @param args [IP-Distributeur] [port-Distributeur] [fichier-scène] [largeur] [hauteur]
     */
    public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {

        String fichier_description = "simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512, desiredlargeur = 50, desiredhauteur = 50;

        String host = "localhost"; //address IP Distributeur
        int port = 1099;

        switch(args.length){
            case 5:
                hauteur = Integer.parseInt(args[4]);
            case 4:
                largeur = Integer.parseInt(args[3]);
            case 3:
                fichier_description = args[2];
            case 2:
                port = Integer.parseInt(args[1]);
            case 1 :
                host = args[0];
            default:
                System.out.println(aide);
        }

        // création d'une fenêtre
        Disp disp = new Disp("Raytracer", largeur, hauteur);

        // Initialisation d'une scène depuis le modèle
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        ArrayList<int[]> imageList = decoupeEnList(largeur, hauteur, desiredlargeur, desiredhauteur);

        //Calculer le temps
//        int numThreads = imageList.size();
//        CountDownLatch latch = new CountDownLatch(numThreads);

        Registry registry = LocateRegistry.getRegistry(host, port);

        ServiceDistributeur sd = (ServiceDistributeur) registry.lookup("distributeur");

        Instant debut = Instant.now();

        AtomicInteger nb_traite = new AtomicInteger(imageList.size());

        do {
            while (imageList.size() != 0) {
                int[] list = imageList.get(0);
                imageList.remove(0);
                Thread thread = new Thread(() -> {
                    NoeudInfo ni = null;
                    ServiceNoeud si = null;
                    try {
                        ni = sd.donnerNoeud();
                        si = ni.getNoeud();
                        System.out.println("Donner la tâche à l'adresse IP " + si.getInformation() + " - x:" + list[0] + " - y:" + list[1] + " - largeur:" + list[2] + " - hauteur:" + list[3]);
                        Image image = si.donnerImage(scene, list[0], list[1], list[2], list[3]);
                        disp.setImage(image, list[0], list[1]);
                        nb_traite.getAndDecrement();
                    } catch (RemoteException e) {
//                        System.out.println("Erreur!!!");
                        try {
                            if (ni != null) {
                                sd.supprimerNoeud(ni.getIp());
                            }
                        } catch (RemoteException ex) {
//                            throw new RuntimeException(ex);
                        }
                        imageList.add(list);
                    }
                });
                thread.start();
            }
        } while (nb_traite.get() != 0);



//        for (int[] list : imageList) {
//            Thread thread = new Thread(() -> {
//                NoeudInfo ni = null;
//                ServiceNoeud si = null;
//                try {
//                    ni = sd.donnerNoeud();
//                    si = ni.getNoeud();
//                    System.out.println("Donner la tâche à l'adresse IP " + si.getInformation() + " - x:" + list[0] + " - y:" + list[1] + " - largeur:" + list[2] + " - hauteur:" + list[3]);
//                    Image image = si.donnerImage(scene, list[0], list[1], list[2], list[3]);
//                    disp.setImage(image, list[0], list[1]);
//                } catch (RemoteException e) {
//                    System.out.println("Erreur!!!");
//                    try {
//                        if(ni != null){
//                            sd.supprimerNoeud(ni.getIp());
//                        }
//                    } catch (RemoteException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                    imageList.add(list);
//                    e.printStackTrace();
//                }
//                latch.countDown();
//            });
//            thread.start();
//        }

//        latch.await();

        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();

        System.out.println("Image calculée en :" + duree + " ms");

    }
}
