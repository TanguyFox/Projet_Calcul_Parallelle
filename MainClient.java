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

public class MainClient {

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
                System.out.println(Client.aide);
        }

        // création d'une fenêtre
        Disp disp = new Disp("Raytracer", largeur, hauteur);

        // Initialisation d'une scène depuis le modèle
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        ArrayList<int[]> imageList = Client.decoupeEnList(largeur, hauteur, desiredlargeur, desiredhauteur);

        //Calculer le temps
        int numThreads = imageList.size();
        CountDownLatch latch = new CountDownLatch(numThreads);

        Registry registry = LocateRegistry.getRegistry(host, port);

        ServiceDistributeur sd = (ServiceDistributeur) registry.lookup("distributeur");

        Instant debut = Instant.now();

        for (int i=0; i<imageList.size(); i++) {
            int[] list = imageList.get(i);
            Thread thread = new Thread(() -> {
                ServiceNoeud si = null;
                try {
                    si = sd.donnerNoeud();
                    System.out.println("Donner la tâche à l'adresse IP " + si.getInformation() + " - x:" + list[0] + " - y:" + list[1] + " - largeur:" + list[2] + " - hauteur:" + list[3]);
                    Image image = si.donnerImage(scene, list[0], list[1], list[2], list[3]);
                    disp.setImage(image, list[0], list[1]);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
            thread.start();
        }

        latch.await();

        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();

        System.out.println("Image calculée en :" + duree + " ms");

    }

}
