package metaheuristic;

import java.util.ArrayList;

import models.Camion;
import models.Client;
import models.Solution;

public class Tabou {

    
    /** 
     * Exécute la méthode du recuit simulé, et retourne la meilleure solution
     * @param camions
     * @param maxIter
     * @param maxTabou
     * @return Solution
     */
    public Solution tabou(ArrayList<Camion> camions, int maxIter, int maxTabou)  {
        long start = System.currentTimeMillis();

        try{
            if(camions == null){
                return null;
            }
            
            Solution xmin = new Solution(camions, new ArrayList<Client>());
            
            ArrayList<Double> allXiFitness = new ArrayList<>();

            ArrayList<ArrayList<Client>> tabouList = new ArrayList<>();
            double fmin = xmin.getFitness();
            Solution xi = xmin.clone();
            
            for(int i = 0; i<maxIter; i++){
                allXiFitness.add(xi.getFitness());
                ArrayList<Solution> filteredNeighbors = new ArrayList<>();
                Solution meilleurSolution;
                //System.out.println("Fitness solution courante : " + fmintemp);
                

                xi.setVoisins();
                
                if(xi.getVoisins().size() != 0){
                    //filtrage VOISIN avec liste Tabou
                    for (Solution voisin : xi.getVoisins()) {
                        if (voisin.getTransformation()==null)
                            continue;
                        boolean isTaboo = false;
                        if(tabouList.contains(voisin.getTransformation())){       
                            isTaboo = true;
                        }
                        if(tabouList.contains(voisin.getReverseTransformation())){
                            isTaboo = true;
                        }
                        if (!isTaboo) {
                            filteredNeighbors.add(voisin);
                        }
                    }

                    // Recherche de la meilleure solution voisine
                    meilleurSolution = null;
                    double meilleurFitnessSolution = Double.POSITIVE_INFINITY;
                    ArrayList<Solution> equalNeighbors = new ArrayList<>();
                    //Pour chaque voisin filtré
                    for(Solution voisin : filteredNeighbors){
                        //Sauvegarde meilleur voisin
                        if(meilleurFitnessSolution >= voisin.getFitness()){
                            meilleurSolution = voisin.clone();
                            meilleurFitnessSolution = voisin.getFitness();
                            equalNeighbors.clear();
                            equalNeighbors.add(voisin);
                        }   
                        else if(voisin.getFitness() == meilleurFitnessSolution)
                        {
                            equalNeighbors.add(voisin);
                        }
                    }       
                    meilleurSolution.reCalculateFitness();
                    xi = meilleurSolution.clone();
                    //Si meilleure fitness que courante
                    if(xi.getFitness() < fmin){
                        //On met à jour la meilleure solution
                        xmin = xi.clone();
                        fmin = xi.getFitness();
                    }

                    //Si pire que meilleure fitness
                    else if(xi.getFitness() > fmin){
                        //Ajout de la transformation
                        tabouList.add(xi.getTransformation());
                        if (tabouList.size() > maxTabou) {
                            tabouList.remove(0);
                        }
                    //Sinon, solution égale 
                    } else {
                        tabouList.add(xi.getTransformation());
                        if (tabouList.size() > maxTabou) {
                            tabouList.remove(0);
                        }
                        int randomIndex = (int) (Math.random() * equalNeighbors.size());
                        meilleurSolution = equalNeighbors.get(randomIndex);                 
                        xi = meilleurSolution.clone();
                    }  
                } 
            }
            // Get elapsed time in milliseconds
            long elapsedTimeMillis = System.currentTimeMillis()-start;
            // Get elapsed time in seconds
            float elapsedTimeSec = elapsedTimeMillis/1000F;
            System.out.println("Tabou terminé ! Temps d'exécution de la méthode : " + elapsedTimeSec + " sec");
            
            xmin.setAllXiFitness(allXiFitness);
            return xmin;
        }
        catch(Exception e){System.out.println("TABOU : " + e);}
        return null;
    }
}