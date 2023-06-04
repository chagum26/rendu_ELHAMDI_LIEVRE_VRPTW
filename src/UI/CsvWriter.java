package UI;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CsvWriter {

    public static void writeColumnToCSV(ArrayList<ArrayList<Double>> columns, String filePath, ArrayList<String> nameColumns) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            int numRows = columns.get(0).size();
            int numCols = columns.size();

            // Écriture des en-têtes de colonne
            for(String nameColumn : nameColumns){
                writer.print(nameColumn + ",");
            }
            writer.println("");

            // Écriture des valeurs de chaque colonne
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols - 1; col++) {
                    writer.print(columns.get(col).get(row) + ",");
                }
                writer.println(columns.get(numCols - 1).get(row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
