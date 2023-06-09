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

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";

    /**
     *
     * @param largeur largeur de l'image
     * @param hauteur hauteur de l'image
     * @param desiredLargeur largeur de morceau
     * @param desiredHauteur hauteur de morceau
     * @return Exemple {[0,0,10,10],[10,10,10,10],[20,20,10,10]...}
     */
    public static ArrayList<int[]> decoupeEnList(int largeur, int hauteur, int desiredLargeur, int desiredHauteur){

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

    public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {

        String fichier_description="simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512, desiredlargeur = 50, desiredhauteur = 50;


        if(args.length > 0){
            fichier_description = args[0];
            if(args.length > 1){
                largeur = Integer.parseInt(args[1]);
                if(args.length > 2)
                    hauteur = Integer.parseInt(args[2]);
            }
        }else{
            System.out.println(aide);
        }

        // création d'une fenêtre
        Disp disp = new Disp("Raytracer", largeur, hauteur);

        // Initialisation d'une scène depuis le modèle
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        ArrayList<int[]> imageList = decoupeEnList(largeur, hauteur, desiredlargeur, desiredhauteur);

        //Calculer le temps
        int numThreads = imageList.size();
        CountDownLatch latch = new CountDownLatch(numThreads);

        Registry registry = LocateRegistry.getRegistry("localhost",1099);

        ServiceDistributeur sd = (ServiceDistributeur) registry.lookup("distributeur");

        Instant debut = Instant.now();

        for(int[] list : imageList){
            Thread thread = new Thread(() -> {
                ServiceNoeud si = null;
                try {
                    si = sd.donnerNoeud();
                    System.out.println("Donner la tâche à l'adresse IP " + si.getInformation()+" - x:"+list[0]+" - y:"+list[1]+" - largeur:"+list[2]+" - hauteur:"+list[3]);
                    Image image = si.donnerImage(scene,list[0],list[1],list[2],list[3]);
                    disp.setImage(image,list[0],list[1]);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
            thread.start();
        }

        latch.await();

        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();

        System.out.println("Image calculée en :"+duree+" ms");

    }
}
