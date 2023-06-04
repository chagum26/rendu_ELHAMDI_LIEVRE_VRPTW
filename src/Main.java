import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import UI.CsvWriter;
import metaheuristic.RandomSolution;
import metaheuristic.RecuitSimule;
import metaheuristic.Tabou;

import java.util.ArrayList;

import models.Camion;
import models.Client;
import models.Solution;
public class Main {
        
    public static String ROOT_DATA = "data/data_to_use/";
    
    //AFFICHAGE RANDOM SOLUTION

        //Si vous souhaitez affiché la solution aléatoire dans la console
        public static boolean PRINT_CONSOLE_QUESTION3 = true;
        //Si vous souhaitez affiché la solution aléatoire dans un JFRAME
        public static boolean PRINT_JFRAME_QUESTION3 = true;

    //PARAMETRAGE RECUIT SIMULE

        //Si vous souhaitez utilisé la métaheuristique : Recuit Simulé
        public static boolean RECUIT_SIMULE = false;

        //Si vous souhaitez affiché la solution finale dans la console
        public static boolean PRINT_CONSOLE_RECUIT = true;
        //Si vous souhaitez affiché la solution finale dans un JFRAME
        public static boolean PRINT_JFRAME_RECUIT = true;
        
        //Paramètres recuit simulé
        public static double TEMPERATURE_INITIAL = 100;
        public static double MU = 0.95;
        public static int N1 = 1000;
        public static int N2 = 2000;

        //Nombre de fois où on répète une métaheuristique (il faudra modifier les paramètres dans le code 
        //si vous ne voulez pas obtenir les mêmes résultats)
        //Ex : Si vous souhaitez augmenté la température graduellement, vous pouvez faire 
        //TEMPERATURE_INITIAL + multiplier * i à l'appel de la fonction du recuit simulé (ligne 250)
        public static int ITERATION_RECUITSIMULE_PER_FILE = 1;

    //PARAMETRAGE TABOU
        //Si vous souhaitez utilisé la métaheuristique : Tabou
        public static boolean METHODE_TABOU = true;

        //Si vous souhaitez affiché la solution finale dans la console
        public static boolean PRINT_CONSOLE_TABOU = true;
        //Si vous souhaitez affiché la solution finale dans un JFRAME
        public static boolean PRINT_JFRAME_TABOU = true;

        //Paramètres Tabou
        public static int TAILLE_LISTE_TABOU = 100;
        public static int MAX_ITER = 500;

        public static int ITERATION_TABOU_PER_FILE = 1;


    //DEBUT MAIN
    public static int NB_DEPOTS;
    public static int NB_CLIENTS;
    public static int MAX_QUANTITY;
    public static void main(String[] args) {
        File dir = new File(ROOT_DATA);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {

            ArrayList<Double> columnRandomSolution;
            ArrayList<ArrayList<Double>> columnsRandomSolution = new ArrayList<>();
            ArrayList<ArrayList<Double>> columnsTabouFitness = new ArrayList<>();
            ArrayList<String> filesName = new ArrayList();

            for (File child : directoryListing) {
                columnRandomSolution = new ArrayList<>();
                
                ArrayList<Client> clients = new ArrayList<>();
                ArrayList<Client> depots = new ArrayList<>();
                try {
                    Scanner FileReader = new Scanner(child);
                    boolean isDepot = false;
                    boolean isClient = false;
                    while (FileReader.hasNextLine()) {
                        String data = FileReader.nextLine();
                        String tmp[] = data.split(":");
        
                        if(data.length() == 0) {
                            isDepot = false;
                            isClient = false;
                        }
        
                        if(isDepot) {
                            String data_depot[] = data.split(" ");
                            Client depot = new Client(
                                data_depot[0],
                                Double.parseDouble(data_depot[1]), 
                                Double.parseDouble(data_depot[2]), 
                                Double.parseDouble(data_depot[3]), 
                                Double.parseDouble(data_depot[4]),
                                0, 
                                0, 
                                true);
                            depots.add(depot);
                        }
        
                        if(isClient) {
                            String data_depot[] = data.split(" ");
                            Client client = new Client(
                                data_depot[0],
                                Double.parseDouble(data_depot[1]), 
                                Double.parseDouble(data_depot[2]), 
                                Double.parseDouble(data_depot[3]), 
                                Double.parseDouble(data_depot[4]),
                                Double.parseDouble(data_depot[5]),
                                Double.parseDouble(data_depot[6]),
                                false);
                            clients.add(client);
                        }
        
                        if(tmp[0].startsWith("NB_DEPOTS")) {
                            NB_DEPOTS = Integer.parseInt(tmp[1].trim());
                        }
        
                        if(tmp[0].startsWith("NB_CLIENTS")) {
                            NB_CLIENTS = Integer.parseInt(tmp[1].trim());
                        }
        
                        if(tmp[0].startsWith("MAX_QUANTITY")) {
                            MAX_QUANTITY = Integer.parseInt(tmp[1].trim());
                        }
        
                        if(tmp[0].startsWith("DATA_DEPOTS")) {
                            isDepot = true;
                        }
                        
                        if(tmp[0].startsWith("DATA_CLIENTS")) {
                            isDepot = false;
                            isClient = true;
                        }
        
                    }
                    FileReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("Fichier : " + child.getName());
                filesName.add(child.getName());

            
                 /*
                 * SOLUTION ALEATOIRE !
                 */
                
                RandomSolution randomSolutions = new RandomSolution();
                ArrayList<Solution> listRandomSolutions = randomSolutions.randomSolution(clients, depots, MAX_QUANTITY, 1);
                columnsRandomSolution.add(columnRandomSolution); 
                System.out.print("");
               
                if(PRINT_JFRAME_QUESTION3 == true){
                    JFrame frameRandom = new JFrame("Affichage solution aléatoire");
                    frameRandom.setTitle("Ma solution aléatoire");
                    frameRandom.setSize(900, 900);  
                    JPanel panelRandom = new JPanel();
                    panelRandom.setSize(frameRandom.getMaximumSize().width, frameRandom.getMaximumSize().height);
    ;
                    frameRandom.getContentPane().add(new LineDrawing(listRandomSolutions.get(0), depots.get(0)));
                                
                    frameRandom.setVisible(true);
                }

                if(PRINT_CONSOLE_QUESTION3 == true){
                    System.out.println("----------------------------");
                    System.out.println("SOLUTION ALEATOIRE GENERE :");
                    System.out.println("Fitness obtenu : " + listRandomSolutions.get(0).getFitness());
                    System.out.println("Nombre de camions : " + listRandomSolutions.get(0).getCamions().size());
                    System.out.println();
                    for (Camion camion : listRandomSolutions.get(0).getCamions()) {
                        camion.printRoutes();
                    }
                }
                
            
            
                
                /*
                 *TEST TABOU
                 */
                
                if(METHODE_TABOU){
                    System.out.println("Méthode Tabou en cours...");
                    ArrayList<String> columnsName = new ArrayList<>();
                    for(int i = 0; i<ITERATION_TABOU_PER_FILE; i++){
                        Tabou tabou = new Tabou();
                        Solution solutionQ5 = tabou.tabou(listRandomSolutions.get(0).getCamions(), MAX_ITER, TAILLE_LISTE_TABOU);
                        columnsTabouFitness.add(solutionQ5.getAllXiFitness());
                        
                        columnsName.add("maxIter = " + MAX_ITER + " maxListTabou = " + TAILLE_LISTE_TABOU);

                        if(PRINT_CONSOLE_TABOU){
                            System.out.println("----------------------------");
                            System.out.println("MA SOLUTION TABOU (itération" + i + ")!\n");
                            
                            System.out.println("FITNESS : " + solutionQ5.getFitness());
                            System.out.println("Camions : " + solutionQ5.getCamions().size());
                            System.out.println();
    
                            
                            for (Camion camion : solutionQ5.getCamions()) {
                                camion.printRoutes();
                            }    
                            System.out.println();
                        }

                        if(PRINT_JFRAME_TABOU == true){
                            JFrame frameTabou = new JFrame("Ma solution TABOU affichée");
                            frameTabou.setTitle("Tabou fichier : " + child.getName() + " : maxIter = " + MAX_ITER + " maxListTabou = " + TAILLE_LISTE_TABOU);
                            frameTabou.setSize(900, 900);  
                            JPanel panelRandom = new JPanel();
                            panelRandom.setSize(frameTabou.getMaximumSize().width, frameTabou.getMaximumSize().height);
            
                            frameTabou.getContentPane().add(new LineDrawing(solutionQ5, depots.get(0)));
                                        
                            frameTabou.setVisible(true);
                        }
                        
                        
                    }
                    String filePath2 = "CSVTabouPerFile.csv";
                    CsvWriter.writeColumnToCSV(columnsTabouFitness, filePath2, columnsName);

                }

                
                /*
                 *TEST RECUIT SIMULE AVEC MODIFICATION MU ET TEMPERATURE !
                 */

                if(RECUIT_SIMULE){
                    System.out.println("Méthode recuit simulé en cours...");
                    ArrayList<Solution> allSolRecuitSimule = new ArrayList<>();
                    ArrayList<ArrayList<Double>> columnsRecuitFitness = new ArrayList<>();
    
                    RecuitSimule recuitSimule;
                    ArrayList<String> columnsName = new ArrayList<>();
                    for(int i = 0; i<ITERATION_RECUITSIMULE_PER_FILE; i++){  
                        recuitSimule = new RecuitSimule();
                        Solution solRecuitSimule = recuitSimule.recuitSimule(listRandomSolutions.get(0).getCamions(), TEMPERATURE_INITIAL, N1, N2, MU);
                        allSolRecuitSimule.add(solRecuitSimule);
                        columnsRecuitFitness.add(solRecuitSimule.getAllXiFitness());
                        columnsName.add("t = " + TEMPERATURE_INITIAL + " mu = " + MU);


                        if(PRINT_CONSOLE_RECUIT){
                            System.out.println("\n");
                            System.out.println("----------------------------");
                            System.out.println("MA SOLUTION RECUIT SIMULE (itération " + i + ")!\n");
                            System.out.println("Fitness obtenue : " + solRecuitSimule.getFitness());
                            System.out.println();
                            System.out.println("avec temperature = " + TEMPERATURE_INITIAL + " et mu : " + MU);
                            System.out.println("N1 = " + N1 + " / n2 = " + N2);
                            System.out.println();
                            System.out.println("Mon nombre de camions : " + solRecuitSimule.getCamions().size());
                            for (Camion camion : allSolRecuitSimule.get(0).getCamions()) {
                                camion.printRoutes();
                            }    
                            System.out.println();
                        }


                        if(PRINT_JFRAME_RECUIT){
                            JFrame frameRecuit = new JFrame("My first JFrame");
                            frameRecuit.setTitle("recuitSimule fichier : " + child.getName() + " : TEMPERATURE_INITIAL = " + TEMPERATURE_INITIAL + " N1 = " + N1 + " N2 = " + N2 + " MU = " + MU);
                            frameRecuit.setSize(900, 900);  
                            JPanel panelRecuit = new JPanel();

                            panelRecuit.setSize(frameRecuit.getMaximumSize().width, frameRecuit.getMaximumSize().height);

                            frameRecuit.getContentPane().add(new LineDrawing(allSolRecuitSimule.get(0), depots.get(0)));
                        
                            frameRecuit.setVisible(true);
                        }
                    }
                    String filePath = "outputRecuit.csv";
                    CsvWriter.writeColumnToCSV(columnsRecuitFitness, filePath, columnsName);
                }
            } 
        } else {
            System.out.println("Aucun fichier dans ce dossier !");
        }

        
        
    }
}
