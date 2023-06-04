package models;

import java.util.ArrayList;
import java.util.Random;

public class Solution implements Cloneable{

    private ArrayList<Solution> voisins;
    private ArrayList<Camion> camions;
    private double fitness;
    private ArrayList<Client> transfo;
    private ArrayList<Double> allXiFitness;

    public Solution(ArrayList<Camion> camions, ArrayList<Client> transfo) {
        voisins = new ArrayList<>();
        this.camions = new ArrayList<>();
        for (Camion camion : camions) {
            if(camion.getRoutes().size() != 0){
                this.camions.add(camion);
            }
        }
        this.fitness = 0;
        for (Camion camion : camions) {
            this.fitness += camion.getFitness();
        }
        this.transfo = (ArrayList<Client>) transfo.clone();
        allXiFitness = new ArrayList<>();
    }

    @Override
    public Solution clone() throws CloneNotSupportedException {
        Solution voisin = (Solution) super.clone();
        voisin.voisins = (ArrayList<Solution>) this.voisins.clone();
        voisin.camions = (ArrayList<Camion>) this.camions.clone();
        voisin.transfo = (ArrayList<Client>) this.transfo.clone();
        return voisin;
    }

    public void cloneVoisins(ArrayList<Solution> voisinsIter) throws CloneNotSupportedException {
        this.voisins = (ArrayList<Solution>) voisinsIter.clone();
    }

    public void cloneCamions(ArrayList<Camion> camionsIter) throws CloneNotSupportedException {
        this.camions = (ArrayList<Camion>) camionsIter.clone();
    }

    public void cloneListClientToSwap(ArrayList<Client> listClientToSwap) throws CloneNotSupportedException {
        this.transfo = (ArrayList<Client>) listClientToSwap.clone();
    }

    public ArrayList<Solution> getVoisins() {
        return this.voisins;
    }

    public void setVoisins(ArrayList<Solution> voisins) {
        this.voisins = voisins;
    }

    public ArrayList<Camion> getCamions() {
        return this.camions;
    }

    public void setCamions(ArrayList<Camion> camions) {
        this.camions = camions;
    }

    public double getFitness() {
        return this.fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    /** 
     * Retourne la transformation effectué pour obtenir cette solution
     * @return ArrayList<Client>
     */
    public ArrayList<Client> getTransformation(){
        return this.transfo;
    }
    
    /** 
     * Retourne l'inverse de la transformation effectué pour obtenir cette solution
     * @return ArrayList<Client>
     */
    public ArrayList<Client> getReverseTransformation(){
        try{
            ArrayList<Client> reverseTransformation = new ArrayList<>();
        reverseTransformation.add(getTransformation().get(1));
        reverseTransformation.add(getTransformation().get(0));
            return reverseTransformation;
        }
        catch(Exception e){}
        return null;
        
    }

    /** 
     * Récupère l'ensemble des fitness parcourues jusqu'à obtention de cette solution
     * @param allXi
     */
    public void setAllXiFitness(ArrayList<Double> allXi){
        allXiFitness = (ArrayList<Double>) allXi.clone();
    }

    public ArrayList<Double> getAllXiFitness(){
        return allXiFitness;
    }

    /** 
     * Execute les opérateurs intra et inter !
     */
    public void setVoisins(){
        this.interOperateur();
        this.intraOperateur();
    }

    /** 
     * Execute les opérateurs inter
     */
    public void interOperateur(){  
        for(Camion camionA : camions){
            for(Client clientA : camionA.getRoutes()){
                ArrayList<Camion> camionsWithoutA = (ArrayList<Camion>) camions.clone();
                camionsWithoutA.remove(camionA);
                for(Camion camionB: camionsWithoutA){
                    for(Client clientB : camionB.getRoutes()){

                        ArrayList<Camion> camionsCopy = (ArrayList<Camion>) camions.clone();
                        camionsCopy.remove(camionsCopy.indexOf(camionA));
                        camionsCopy.remove(camionsCopy.indexOf(camionB));

                        ArrayList<Client> transformation = new ArrayList<>();
                        transformation.add(clientA);
                        transformation.add(clientB);
                        try{

                            //Opérateur interRelocate
                            Camion camionAcopy = (Camion) camionA.clone();
                            Camion camionBcopy = (Camion) camionB.clone();

                            if(interRelocate(camionAcopy, camionBcopy, camionAcopy.getRoutes().indexOf(clientA), camionBcopy.getRoutes().indexOf(clientB))){
                                addVoisinInter(camionsCopy, camionAcopy, camionBcopy, transformation);                           
                            }

                            if(camionB.getRoutes().indexOf(clientB) == 0){
                                camionAcopy = (Camion) camionA.clone();
                                camionBcopy = (Camion) camionB.clone();

                                if(interRelocate(camionAcopy, camionBcopy, camionAcopy.getRoutes().indexOf(clientA),-1)){
                                    addVoisinInter(camionsCopy, camionAcopy, camionBcopy, transformation);    
                                }
                            }

                            //Opérateur interExchange
                            camionAcopy = (Camion) camionA.clone();
                            camionBcopy = (Camion) camionB.clone();
                            if(interExchange(camionAcopy, camionBcopy, camionAcopy.getRoutes().indexOf(clientA), camionBcopy.getRoutes().indexOf(clientB))){
                                addVoisinInter(camionsCopy, camionAcopy, camionBcopy, transformation);        
                            }

                            //Opérateur interCrossExchange
                            camionAcopy = (Camion) camionA.clone();
                            camionBcopy = (Camion) camionB.clone();
                            if(interCrossExchange(camionAcopy, camionBcopy, camionAcopy.getRoutes().indexOf(clientA), camionBcopy.getRoutes().indexOf(clientB))){
                                addVoisinInter(camionsCopy, camionAcopy, camionBcopy, transformation);   
                            } 
                        }
                        catch(Exception e){System.out.println("interOperateur : " + e);}
                    }
                }
            }
        }
    }

    /** 
     * Execute les opérateurs intra
     */
    public void intraOperateur(){
        for(Camion camionA : camions){
            ArrayList<Camion> camionsCopy = (ArrayList<Camion>) camions.clone();
            camionsCopy.remove(camionA);
            try{
                Camion camionACopy = (Camion) camionA.clone();

                //Opérateur intraReverse
                if(intraReverse(camionACopy)){
                    addVoisinIntra(camionsCopy, camionACopy, new ArrayList<>());
                }

                for(Client clientA : camionA.getRoutes()){
                    for(Client clientB : camionA.getRoutes()){

                        ArrayList<Client> transfo = new ArrayList<>();
                        transfo.add(clientA);
                        transfo.add(clientB);

                        camionACopy = (Camion) camionA.clone();
                        camionsCopy.remove(camionA);
                        if(!(clientA.getName()==clientB.getName())){

                            //Opérateur intraExchange
                            if(intraExchange(camionACopy, camionA.getRoutes().indexOf(clientA), camionA.getRoutes().indexOf(clientB))){
                                addVoisinIntra(camionsCopy, camionACopy, transfo);
                            }

                            //Opérateur intraRelocate
                            if(camionA.getRoutes().indexOf(clientB) == 0){
                                    camionACopy = (Camion) camionA.clone();
                                    if(intraRelocate(camionACopy, camionA.getRoutes().indexOf(clientA), -1)){
                                        addVoisinIntra(camionsCopy, camionACopy, transfo);
                                    }
                                }

                            camionACopy = (Camion) camionA.clone();
                            if(intraRelocate(camionACopy, camionA.getRoutes().indexOf(clientA), camionA.getRoutes().indexOf(clientB))){
                                addVoisinIntra(camionsCopy, camionACopy, transfo);
                            }    
                        }
                    }
                }
            }
            catch(Exception e){System.out.println("intraOperateur : " + e);}  
        }
    }

    /** 
     * Crée un nouveau voisin après calcul d'un opérateur intra
     * @param camions
     * @param camionA
     * @param transformation
     */
    public void addVoisinIntra(ArrayList<Camion> camions, Camion camionA, ArrayList<Client> transformation){
        ArrayList<Camion> camionsCopy = (ArrayList<Camion>) camions.clone();
        camionsCopy.add(camionA);
        voisins.add(new Solution(camionsCopy, transformation));
    }

    public boolean intraRelocate(Camion camionA, int indexClientA, int indexClientB){
        
        if(indexClientB + 1 == indexClientA){
            return false;
        }  
       
        camionA.addClientAToRouteAfterClientB(camionA.getRoutes().get(indexClientA), indexClientB+1);
        if(indexClientA < indexClientB){
            camionA.getRoutes().remove(indexClientA);
        } else {
            camionA.getRoutes().remove(indexClientA+1);
        }
 
        return camionA.checkContraintes();
    }

    public boolean intraExchange(Camion camionA, int indexClientA, int indexClientB){

        if(indexClientA == indexClientB){
            return false;
        }

        try{
            Camion camionACopy = (Camion) camionA.clone();

            camionA.addClientAToRouteAfterClientB(camionACopy.getRoutes().get(indexClientB), indexClientA+1);
    
            camionA.getRoutes().remove(indexClientA);
            
            camionA.addClientAToRouteAfterClientB(camionACopy.getRoutes().get(indexClientA), indexClientB+1);
            camionA.getRoutes().remove(indexClientB);

            return camionA.checkContraintes();
        }
        catch(Exception e){System.out.println(e);}
        return false;
        
    }

    public boolean intraReverse(Camion camionA){

        ArrayList<Client> routesCopy = new ArrayList<>();

        for(Client client : camionA.getRoutes()){
            routesCopy.add(0, client);
        }
        
        camionA.setRoutes(routesCopy);
            
        return camionA.checkContraintes();
    }
    
    /** 
     * Crée un nouveau voisin après calcul d'un opérateur inter
     * @param camions
     * @param camionA
     * @param transformation
     */
    public void addVoisinInter(ArrayList<Camion> camions, Camion camionA, Camion camionB, ArrayList<Client> transformation){
        ArrayList<Camion> camionsCopy = (ArrayList<Camion>) camions.clone();
        camionsCopy.add(camionA);
        camionsCopy.add(camionB);
        voisins.add(new Solution(camionsCopy, transformation));
    }

    public boolean interRelocate(Camion camionA, Camion camionB, int indexClientA, int indexClientB){    
    
        camionB.addClientAToRouteAfterClientB(camionA.getRoutes().get(indexClientA), indexClientB + 1);
        camionA.getRoutes().remove(indexClientA);     

        return camionB.checkContraintes() && camionA.checkContraintes();
    }

    public boolean interExchange(Camion camionA, Camion camionB, int indexClientA, int indexClientB){
        
        camionB.addClientAToRouteAfterClientB(camionA.getRoutes().get(indexClientA), indexClientB+1);
        camionA.addClientAToRouteAfterClientB(camionB.getRoutes().get(indexClientB), indexClientA+1);

        camionB.getRoutes().remove(indexClientB);
        camionA.getRoutes().remove(indexClientA);
         
        return camionB.checkContraintes() && camionA.checkContraintes();
    }

    public boolean interCrossExchange(Camion camionA, Camion camionB, int indexClientA, int indexClientB){

        if(indexClientA == 0 && indexClientB == 0){
            return false;
        }
        ArrayList<Client> listFromCamionA = new ArrayList<>();
        ArrayList<Client> listFromCamionB = new ArrayList<>();

        while(indexClientA != camionA.getRoutes().size()){
            listFromCamionA.add(camionA.getRoutes().get(indexClientA));
            camionA.getRoutes().remove(indexClientA);
        }

        while(indexClientB != camionB.getRoutes().size()){
            listFromCamionB.add(camionB.getRoutes().get(indexClientB));
            camionB.getRoutes().remove(indexClientB);
        }

        for(Client client : listFromCamionA){
            camionB.getRoutes().add(client);
        }

        for(Client client : listFromCamionB){
            camionA.getRoutes().add(client);
        }

        return camionA.checkContraintes() && camionB.checkContraintes(); 
    }

    public void reCalculateFitness(){
        this.fitness = 0;
        for (Camion camion : this.camions) {
            this.fitness += camion.getFitness();
        }
    }

    /** 
     * Génère un voisin aléatoire en 500 tentatives
     * @return ArrayList<Solution>
     */
    public ArrayList<Solution> randomVoisin(){
        this.voisins = new ArrayList<>();
        Random random = new Random();
        int randomIndex = random.nextInt(2);

        for(int i = 0; i < 500; i++){
            if(this.voisins.size() > 0){
                break;
            }  
            ArrayList<Camion> camionsCopy = (ArrayList<Camion>) camions.clone();

            Camion camionA = camionsCopy.get(random.nextInt(camions.size()));
            camionsCopy.remove(camionsCopy.indexOf(camionA));
            if(camionA.getRoutes().size() <= 1){
                randomIndex = 0;
            }         
            switch(randomIndex){
                //interOperateur
                case 0: 
                    //récupération camionA et camionB     
                    try{
                        Camion camionB = camionsCopy.get(random.nextInt(camions.size()-1));
                        camionsCopy.remove(camionsCopy.indexOf(camionB));
                        //récupération clientA et clientB
                        Client clientA = camionA.getRoutes().get(random.nextInt(camionA.getRoutes().size()));
                        Client clientB = camionB.getRoutes().get(random.nextInt(camionB.getRoutes().size()));

                        Camion camionAcopy = (Camion) camionA.clone();
                        Camion camionBcopy = (Camion) camionB.clone();

                        ArrayList<Client> transformation = new ArrayList<>();
                        transformation.add(clientA);
                        transformation.add(clientB);

                        int randomIndexInterOperateur = random.nextInt(3);
                        switch(randomIndexInterOperateur){
                            case 0:
                                if(interRelocate(camionAcopy, camionBcopy, camionA.getRoutes().indexOf(clientA), camionB.getRoutes().indexOf(clientB))){                                   
                                    addVoisinInter(camionsCopy, camionAcopy, camionBcopy, transformation);                           
                                }

                                camionAcopy = (Camion) camionA.clone();
                                camionBcopy = (Camion) camionB.clone();
                                if(camionB.getRoutes().indexOf(clientB) == 0){
                                    if(interRelocate(camionAcopy, camionBcopy, camionA.getRoutes().indexOf(clientA) ,-1)){
                                        addVoisinInter(camionsCopy, camionAcopy, camionBcopy, transformation);    
                                    }   
                                }
                                 
                                
                                break;

                            case 1:
                                //Opérateur interExchange
                                if(interExchange(camionAcopy, camionBcopy, camionA.getRoutes().indexOf(clientA), camionB.getRoutes().indexOf(clientB))){
                                    addVoisinInter(camionsCopy, camionAcopy, camionBcopy, transformation);        
                                }
                                break;

                            case 2:
                                //Opérateur interCrossExchange
                                if(interCrossExchange(camionAcopy, camionBcopy, camionA.getRoutes().indexOf(clientA), camionB.getRoutes().indexOf(clientB))){
                                    addVoisinInter(camionsCopy, camionAcopy, camionBcopy, transformation);       
                                }
                                break;

                            default:
                                break;
                        }            
                    }
                    catch(Exception e){System.out.println("randomSolution INTER : " + e.getMessage());}
                    break;
                //intraOperateur
                case 1:
                    try{

                        Camion getClientCamion = camionA.clone();

                        Client clientA = getClientCamion.getRoutes().get(random.nextInt(getClientCamion.getRoutes().size()));
                        getClientCamion.getRoutes().remove(getClientCamion.getRoutes().indexOf(clientA));

                        Client clientB;
                        if(getClientCamion.getRoutes().size() < 2){
                            clientB = getClientCamion.getRoutes().get(0);
                        } else {
                            clientB = getClientCamion.getRoutes().get(random.nextInt(getClientCamion.getRoutes().size()-1));
                        }
                        
                        Camion camionAcopy = (Camion) camionA.clone();

                        ArrayList<Client> transformation = new ArrayList<>();
                        transformation.add(clientA);
                        transformation.add(clientB);

                        int randomIndexIntraOperateur = random.nextInt(3);
                       
                        switch(randomIndexIntraOperateur){
                            case 0:
                                if(intraReverse(camionAcopy)){
                                    addVoisinIntra(camionsCopy, camionAcopy, new ArrayList<>());
                                }                            
                                break;

                            case 1:
                                //Opérateur intraExchange
                                if(intraExchange(camionAcopy, camionA.getRoutes().indexOf(clientA), camionA.getRoutes().indexOf(clientB))){
                                    addVoisinIntra(camionsCopy, camionAcopy, transformation);
                                }
                                break;

                            case 2:
                                //Opérateur intraRelocate
                                if(camionA.getRoutes().indexOf(clientB) == 0){
                                    camionAcopy = (Camion) camionA.clone();
                                    if(intraRelocate(camionAcopy, camionA.getRoutes().indexOf(clientA), -1)){
                                        addVoisinIntra(camionsCopy, camionAcopy, transformation);
                                    }
                                }

                                if(camionA.getRoutes().indexOf(clientB) == 0){
                                    camionAcopy = (Camion) camionA.clone();
                                    if(intraRelocate(camionAcopy, camionA.getRoutes().indexOf(clientA), camionA.getRoutes().indexOf(clientB))){
                                        
                                        addVoisinIntra(camionsCopy, camionAcopy, transformation);
                                        
                                    }    
                                }    
                                break;
                        }
                    }
                    catch(Exception e){System.out.println("randomSolution INTRA : " + e.getMessage());}
                    break;

                default:
                    break;
            }
        } 
        return voisins;
    }
}
