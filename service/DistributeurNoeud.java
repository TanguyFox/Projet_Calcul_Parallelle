import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class DistributeurNoeud implements ServiceDistributeur {

    private List<ServiceImage> neouds = new ArrayList<>();
    private int index;
    public DistributeurNoeud() {
        this.index = 0;
    }

    public void enregistrerNoeud(ServiceImage noeud) throws RemoteException {
        this.neouds.add(noeud);
    }

    @Override
    public void enregistrerNoeud(ServiceDistributeur noeud) throws RemoteException {

    }

    public ServiceImage donnerNoeud() throws RemoteException {
        if (index < neouds.size()) {
            ServiceImage n = neouds.get(index);
            index++;
            return n;
        } else {
            index = 0;
            ServiceImage n = neouds.get(index);
            index++;
            return n;
        }
    }
}