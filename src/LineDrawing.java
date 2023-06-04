

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JComponent;

import models.Camion;
import models.Client;
import models.Solution;

class LineDrawing extends JComponent {
  
    public static int MULTIPLIER = 9;

    private ArrayList<Camion> listCamion;
    private int depot_posx;
    private int depot_posy;

    public LineDrawing(Solution solution, Client depot)
    {
        this.listCamion = (ArrayList<Camion>) solution.getCamions().clone();
        this.depot_posx = (int) depot.getPos_x();
        this.depot_posy = (int) depot.getPos_y();
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;   
        g.setColor(Color.BLUE);
        g.fillRect((int) depot_posx* MULTIPLIER, (int) depot_posy * MULTIPLIER, 7, 7);
        g.setColor(Color.RED);
        for (Camion camion : listCamion) {
            for (Client client : camion.getRoutes()) {
                // Paint the target (selector)
                g.setColor(Color.RED);
                g.fillOval((int) client.getPos_x() * MULTIPLIER, (int) client.getPos_y() * MULTIPLIER, 5, 5);
                g.setColor(Color.RED);
        
            }
            if(camion.getRoutes().size() > 1)
            {
                g.drawLine(
                    depot_posx * 9,
                    depot_posy * 9,
                    (int) camion.getRoutes().get(0).getPos_x() * MULTIPLIER,
                    (int) camion.getRoutes().get(0).getPos_y() * MULTIPLIER
                );
                for(int i = 0; i < camion.getRoutes().size() - 1 ; i ++)
                {
                    
                    g.drawLine(
                        (int) camion.getRoutes().get(i).getPos_x()* MULTIPLIER, 
                        (int) camion.getRoutes().get(i).getPos_y()* MULTIPLIER, 
                        (int) camion.getRoutes().get(i+1).getPos_x()* MULTIPLIER, 
                        (int) camion.getRoutes().get(i+1).getPos_y()* MULTIPLIER
                    );
                }
                g.setColor(Color.RED);
                g.drawLine(
                    (int) camion.getRoutes().get(camion.getRoutes().size() - 1).getPos_x() * MULTIPLIER, 
                    (int) camion.getRoutes().get(camion.getRoutes().size() - 1).getPos_y() * MULTIPLIER,
                    depot_posx * MULTIPLIER, 
                    depot_posy * MULTIPLIER
                );
            }
            else if(camion.getRoutes().size() == 1){
                g.setColor(Color.RED);
                g.drawLine(
                    depot_posx *MULTIPLIER, 
                    depot_posy *MULTIPLIER, 
                    (int) camion.getRoutes().get(0).getPos_x() *MULTIPLIER, 
                    (int) camion.getRoutes().get(0).getPos_y()*MULTIPLIER
                );
                g.setColor(Color.RED);
                g.drawLine(
                    (int) camion.getRoutes().get(camion.getRoutes().size() - 1).getPos_x() *MULTIPLIER, 
                    (int) camion.getRoutes().get(camion.getRoutes().size() - 1).getPos_y() *MULTIPLIER,
                    depot_posx *MULTIPLIER, 
                    depot_posy *MULTIPLIER
                );
                
            }
        }
    }

}