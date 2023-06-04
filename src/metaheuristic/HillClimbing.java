package metaheuristic;

import java.util.ArrayList;

import models.Camion;
import models.Client;
import models.Solution;

public class HillClimbing {
    
    /** 
     * Execute la méthode de descente, et retourne la meilleure solution obtenue
     * @param camions
     * @return Solution
     */
    public Solution hillClimbing(ArrayList<Camion> camions){
        Solution solution_initial = new Solution(camions, new ArrayList<Client>());
        double fitness = solution_initial.getFitness();
        boolean isBetter = false;  

        try{
            Solution solution = (Solution) solution_initial.clone();

            do{
                solution.setVoisins();
                isBetter = false;
                for (Solution voisin : solution.getVoisins()) {
                    if(fitness > voisin.getFitness())
                    {
                        isBetter = true;
                        solution = (Solution) voisin.clone();
                        fitness = solution.getFitness();               
                    }
                }
            }while(isBetter);
            return solution;
        } 
        catch(Exception e){System.out.println("erreur HC " + e);}
        return null;
    }
}