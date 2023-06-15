package noeud;

import service.ServiceNoeud;

import java.io.Serializable;

public class NoeudInfo implements Serializable {
    private ServiceNoeud noeud;
    private String ip;

    public NoeudInfo(String ip, ServiceNoeud noeud){
        this.ip = ip;
        this.noeud = noeud;
    }

    public ServiceNoeud getNoeud() {
        return noeud;
    }

    public String getIp() {
        return ip;
    }
}
