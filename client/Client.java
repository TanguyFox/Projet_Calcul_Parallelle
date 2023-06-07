package client;

import java.util.ArrayList;
import java.util.List;

public class Client {

    ArrayList<int[]> decoupeEnList(int largeur, int hauteur, int desiredLargeur, int desiredHauteur){

        ArrayList<int[]> imageList = new ArrayList<>();

        for (int x = 0; x < largeur; x += desiredLargeur) {
            for (int y = 0; y < hauteur; y += desiredHauteur) {
                int l = Math.min(desiredLargeur, largeur - x);
                int h = Math.min(desiredHauteur, hauteur - y);
                imageList.add(new int[]{x, y, largeur, hauteur});
            }
        }

        for (int[] crop : imageList) {
            int x = crop[0];
            int y = crop[1];
            int l = crop[2];
            int h = crop[3];
            System.out.println("(" + x + ", " + y + ", " + l + ", " + h + ")");
        }
        return imageList;
    }

    public static void main(String[] args) {





    }
}
