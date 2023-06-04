package models;

import java.util.ArrayList;

public class Camion implements Cloneable{
    private int idCamion;
    private String name;
    private double pos_x;
    private double pos_y;
    private static int nbCamion = 0;
    private ArrayList<Client> routes;
    private double time;
    private double quantity;
    private double quantity_FROM_DEPOT;
    private Client depot;
    private float fitness;  

    public Camion(String name, Client depot, double quantity) {
        this.idCamion = nbCamion++;
        this.name = name + idCamion;
        this.pos_x = depot.getPos_x();
        this.pos_y = depot.getPos_y();
        this.quantity = quantity;
        this.quantity_FROM_DEPOT = quantity;
        this.routes = new ArrayList<Client>();
        this.time = 0;
        this.depot = depot;
        this.fitness = 0;
    }

    
    /** 
     * @return Camion
     * @throws CloneNotSupportedException
     */
    @Override
    public Camion clone() throws CloneNotSupportedException {
        Camion camion = (Camion) super.clone();
        camion.routes = (ArrayList<Client>) this.routes.clone();
        return camion;
    }

    
    /** 
     * @param clients
     * @throws CloneNotSupportedException
     */
    public void cloneRoute(ArrayList<Client> clients) throws CloneNotSupportedException {
        this.routes = (ArrayList<Client>) clients.clone();
    }

    
    public int getIdCamion() {
        return this.idCamion;
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

    public void setPos(double pos_x, double pos_y){
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    public ArrayList<Client> getRoutes() {
        return this.routes;
    }


    
    /** 
     * Retourne le dernier client d'une tournée de camion
     * @return Client
     */
    public Client getLastClientFromRoute() {
        return this.routes.get(this.routes.size() - 1);
    }

    public void setRoutes(ArrayList<Client> routes) {
        this.routes = routes;
    }

    
    /** 
     * Ajoute un client à la tournée de camion !
     * @param client
     * @param isWaiting
     */
    public void addClientToRoute(Client client, boolean isWaiting) {
        this.routes.add(client);
        if(isWaiting){
            setTime(client.getReadyTime());
        } else {
            addTime(distance_euclidienne(
                this.pos_x,
                client.getPos_x(),
                this.pos_y,
                client.getPos_y()
                )
            );
        }
        setPos(client.getPos_x(), client.getPos_y());
        addTime(client.getService());
        removeQuantity(client.getDemande());
    }

    public double getTime() {
        return this.time;
    }

    public void addTime(double time) {
        this.time += time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public void removeQuantity(double quantity) {
        this.quantity -= quantity;
    }

    public Client getDepot(){
        return this.depot;
    }

    public static void resetNbCamion() {
        nbCamion = 0;
    }

    public void removeClientFromRoute(Client client){
        this.routes.remove(client);
    }

    public static double distance_euclidienne(double x1, double x2, double y1, double y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    public float getFitness(){
        double x = depot.getPos_x();
        double y = depot.getPos_y();
        float fitness = 0;
        for(Client client : getRoutes()){
            fitness += distance_euclidienne(x, client.getPos_x(), y, client.getPos_y());
            x = client.getPos_x();
            y = client.getPos_y();
        }
        fitness += distance_euclidienne(x, depot.getPos_x(), y, depot.getPos_y());
        return fitness;

    }

    public void addTimeLastClientToDepot(){
        Client lastClient = this.routes.get(this.routes.size() - 1);
        this.time += distance_euclidienne(
                        lastClient.getPos_x(), 
                        depot.getPos_x(), 
                        lastClient.getPos_y(),
                        depot.getPos_y()
        );
    }

    /** 
     * Ajoute le client A à l'index du client B
     * @param clientA
     * @param indexClientB
     */
    public void addClientAToRouteAfterClientB(Client clientA, int indexClientB) {
        this.routes.add(indexClientB, clientA);
    }

    /** 
     * Calcule le temps et la quantité restante,
     * Si contraintes respectées, retourne vrai, sinon faux
     * @return boolean
     */
    public boolean getTimeQuantityRecalculate(){
        this.pos_x = depot.getPos_x();
        this.pos_y = depot.getPos_y();
        this.time = 0;
        this.quantity = quantity_FROM_DEPOT;
        boolean isPossible = true;
        if(getRoutes().size() > 0){
            for (Client client : getRoutes()) {
                addTime(distance_euclidienne(client.getPos_x(), this.pos_x, client.getPos_y(), this.pos_y));
                //Si readyTime client inférieur au temps, non possible
                if(getTime() > client.getDueTime()){
                    isPossible = false;
                    break;
                }
                //Si readyTime client supérieur au temps, on attend jusqu'au readyTime client
                if(getTime() < client.getReadyTime()) {
                    setTime(client.getReadyTime());
                }
                addTime(client.getService());
                removeQuantity(client.getDemande());
                setPos(client.getPos_x(), client.getPos_y());

            }
            addTimeLastClientToDepot();
        }

        return isPossible;
    }

    /** 
     * Vérifie les contraintes du VRPTW
     * @return boolean
     */
    public boolean checkContraintes(){
        return (getTimeQuantityRecalculate() && this.time < depot.getDueTime() && this.quantity >= 0);     
    }

    public void printRoutes(){
        System.out.printf(getName() + ", fitness = %.2f :", getFitness());
        for (Client client : getRoutes()) {   
            if(getRoutes().indexOf(client) == getRoutes().size() - 1 )
                System.out.print(client.getName() + " ");
            else
                System.out.print(client.getName() + " => ");
        }
        System.out.println();
    }
}
