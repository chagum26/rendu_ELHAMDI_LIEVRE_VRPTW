package metaheuristic;

import java.util.ArrayList;
import java.util.Random;

import models.Camion;
import models.Solution;

public class RecuitSimule {

    
    /** 
     * Exécute la méthode du recuit simulé, et retourne la meilleure solution
     * @param camions
     * @param temperature
     * @param n1
     * @param n2
     * @param mu
     * @return Solution
     */
    public Solution recuitSimule(ArrayList<Camion> camions, double temperature, int n1, int n2, double mu){
        long start = System.currentTimeMillis();
        if(camions == null){
            return null;
        }
        Solution xmin = new Solution(camions, new ArrayList<>());

        ArrayList<Double> allXiFitness = new ArrayList<>();

        double fmin = xmin.getFitness();
        double tk = temperature;
        try{
            
            Solution xi = xmin.clone();
            for(int k = 0; k < n1; k++){
                
                for(int l = 1; l < n2; l++){
                    
                    //System.out.println(k + " " + l +  " Fitness solution courante : " + xi.getFitness());
                    if(l == 1)
                        allXiFitness.add(xi.getFitness());

                    Random random = new Random();
                    int randomIndex;
                    Solution y;
           
                    if(xi.randomVoisin().size()>0){
                        randomIndex = random.nextInt(xi.getVoisins().size());
                        y = xi.getVoisins().get(randomIndex).clone();
                    } else {
                        xi.interOperateur();
                        xi.intraOperateur();
                        randomIndex = random.nextInt(xi.getVoisins().size());
                        y = xi.getVoisins().get(randomIndex).clone();
                    }                

                    double delta = y.getFitness() - xi.getFitness();

                    if(delta <= 0){
                        xi = y;
                        if(xi.getFitness() < fmin){
                            xmin = xi.clone();
                            fmin = xi.getFitness();
                        }
                    } else {
                        double p = Math.random();
                        if(p < Math.exp(-delta/tk)){
                            xi = y.clone();
                        }
                    }
                }
                tk = mu * tk;
            }

            
            // Temps en milliseconde
            long elapsedTimeMillis = System.currentTimeMillis()-start;
            // Temps en seconde
            float elapsedTimeSec = elapsedTimeMillis/1000F;
            System.out.println("Recuit simulé fini ! Temps d'exécution : " + elapsedTimeSec + " sec");

            xmin.setAllXiFitness(allXiFitness);

            
            return xmin; 
        }
        catch(Exception e){System.out.println("RecuitSimulé : " + e);}
        return null;

    }
}
