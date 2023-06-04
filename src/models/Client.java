package models;

import java.util.Comparator;

public class Client implements Comparator, Cloneable{
    private int idClient;
    private String name;
    private double pos_x;
    private double pos_y;
    private double readyTime;
    private double dueTime;
    private double demande;
    private double service;
    private boolean isDepot;
    private static int nbClients = 0;

    public Client(String name, double pos_x, double pos_y, double readyTime, double dueTime, double demande, double service, boolean isDepot) {
        this.idClient = nbClients++;
        this.name = name;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
        this.demande = demande;
        this.service = service;
        this.isDepot = isDepot;
    }

    @Override
    public Client clone() throws CloneNotSupportedException {
        return (Client) super.clone();
    }
    
    public String toString(){
        System.out.println(idClient);
        System.out.println(name);
        System.out.println(pos_x);
        System.out.println(pos_y);
        System.out.println(readyTime);
        System.out.println(dueTime);
        System.out.println(demande);
        System.out.println(service);
        System.out.println(isDepot);
        return name;
    }

    public int getIdClient() {
        return this.idClient;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPos_x() {
        return this.pos_x;
    }

    public void setPos_x(double pos_x) {
        this.pos_x = pos_x;
    }

    public double getPos_y() {
        return this.pos_y;
    }

    public void setPos_y(double pos_y) {
        this.pos_y = pos_y;
    }

    public double getReadyTime() {
        return this.readyTime;
    }

    public void setReadyTime(double readyTime) {
        this.readyTime = readyTime;
    }

    public double getDueTime() {
        return this.dueTime;
    }

    public void setDueTime(double dueTime) {
        this.dueTime = dueTime;
    }

    public double getDemande() {
        return this.demande;
    }

    public void setDemande(double demande) {
        this.demande = demande;
    }

    public double getService() {
        return this.service;
    }

    public void setService(double service) {
        this.service = service;
    }

    public boolean isIsDepot() {
        return this.isDepot;
    }

    public boolean getIsDepot() {
        return this.isDepot;
    }

    public void setIsDepot(boolean isDepot) {
        this.isDepot = isDepot;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Client myObj1 = (Client)o1;
        Client myObj2 = (Client)o2;
        if(myObj1.getReadyTime() > myObj2.getReadyTime()) {
            return 1;
        } else if(myObj1.getReadyTime() == myObj2.getReadyTime()) {
            return 0;
        } else {
            return -1;
        }
    }
}
