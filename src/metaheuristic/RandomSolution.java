package metaheuristic;

import java.util.ArrayList;
import java.util.Random;

import models.Camion;
import models.Client;
import models.Solution;


public class RandomSolution {

    
    /** 
     *  Méthode permettant la génération d'une ou plusieurs solutions aléatoires.
     * @param clients
     * @param depots
     * @param MAX_QUANTITY
     * @param maxIter
     * @return ArrayList<Solution>
     */
    public ArrayList<Solution> randomSolution(ArrayList<Client> clients, ArrayList<Client> depots, double MAX_QUANTITY, int maxIter){
        ArrayList<Solution> randomSolutions = new ArrayList<>();
        for(int i = 0; i<maxIter; i++){
            Random randomGenerator = new Random();
            ArrayList<Client> livraisonClients = (ArrayList<Client>) clients.clone();
            ArrayList<Camion> camions = new ArrayList<Camion>();
            
            //Tant qu'il y a des clients à livrer
            while(livraisonClients.size()>0){
                //On initialise une liste pour tirer au sort
                ArrayList<Client> clientsTSR = (ArrayList<Client>) livraisonClients.clone();
                Camion camion = new Camion("Camion", depots.get(0), MAX_QUANTITY);
                camions.add(camion);
                //Tant qu'on peut tirer au sort
                while(clientsTSR.size()>0){
                    boolean isLivrable = false;
                    boolean isWaiting = false;
                    //On tire au sort un client
                    int randomClientIndex = randomGenerator.nextInt(clientsTSR.size());
                    Client randomClient = clientsTSR.get(randomClientIndex);
                    clientsTSR.remove(randomClient);
                    //On calcule la distance Camion<->Client et Client<->Dépot
                    double distance_CamionClient = distance_euclidienne(randomClient.getPos_x(), camion.getPos_x(), randomClient.getPos_y(), camion.getPos_y());
                    double distance_ClientDepot = distance_euclidienne(depots.get(0).getPos_x(), randomClient.getPos_x(), depots.get(0).getPos_y(), randomClient.getPos_y());
                
                    //Si on arrive avant qu'il ne soit plus dispo
                    if(camion.getTime() + distance_CamionClient < randomClient.getDueTime()){
                        //Si on arrive quand il est disponible
                        if(camion.getTime() + distance_CamionClient >= randomClient.getReadyTime()){
                            //Si on a le temps de rentrer au dépot ensuite
                            if(camion.getTime() + distance_CamionClient + distance_ClientDepot + randomClient.getService() < depots.get(0).getDueTime()){
                                //Alors on livre !
                                isLivrable = true;
                            }
                        }
                        //Si on arrive avant de le livrer
                        else if(camion.getTime() + distance_CamionClient < randomClient.getReadyTime()){
                            //Si on a le temps de rentrer au dépot ensuite
                            if(randomClient.getReadyTime() + distance_ClientDepot + randomClient.getService() < depots.get(0).getDueTime()){
                                //Alors on livre !
                                isLivrable = true;
                                //Et on attend que le client soit disponible
                                isWaiting = true;
                            }
                        }
                    }

                    if(isLivrable) {
                        camion.addClientToRoute(randomClient, isWaiting);
                        
                        livraisonClients.remove(randomClient);
                    }
                
                }
                camion.addTimeLastClientToDepot();
            }
            randomSolutions.add(new Solution(camions, new ArrayList<>()));
        }
        
        return randomSolutions;
    }

    
    /** 
     * Calcule la distance euclidienne entre 2 points et retourne le résultat (double)
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return double
     */
    public static double distance_euclidienne(double x1, double x2, double y1, double y2) {
        return Math.ceil(Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)));
    }
}