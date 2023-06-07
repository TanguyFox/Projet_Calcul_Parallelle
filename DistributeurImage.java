import java.rmi.RemoteException;

import raytracer.Image;
import raytracer.Scene;

public class DistributeurImage implements ServiceDistributeur {

    @Override
    public Image donnerImage(Scene scene, int x, int y, int l, int h) throws RemoteException {

       return scene.compute(x, y, l, h);

        
        
    }
    
}
