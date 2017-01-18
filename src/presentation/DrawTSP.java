/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Vector;
import exceptions.*;

/**
 *
 * @author Pau
 */
public class DrawTSP {
    public static void drawNode(Graphics2D g, int x, int y, int alt, int ampl){
        g.setColor(Color.BLACK);
        g.fillOval(x, y, ampl, alt);
    }
    public static void drawNodeSol(Graphics2D g, int x, int y, int alt, int ampl){
        g.setColor(Color.RED);
        g.fillOval(x, y, ampl, alt);
    }
     public static void drawNodeWithPacket(Graphics2D g, int x, int y, int alt, int ampl){
        g.setColor(Color.GREEN);
        g.fillOval(x, y, ampl, alt);
    }
    
    public static void drawLine(Graphics g, int x1, int y1, int x2, int y2){
        g.drawLine(x1, y1, x2, y2);
    }
    
    public static void drawLine(Graphics2D g, int x1, int y1, int x2, int y2, Color color){
        g.setColor(color);
        
        g.draw(new Line2D.Double(x1, y1, x2, y2));
    }
    public static void DrawMap(ViewController vc, javax.swing.JPanel mapDrawPanel) throws DrawException{
        if(vc.getSolutionString().isEmpty()) {
            throw new DrawException("The route is not calculated!");
        }
        else {
            mapDrawPanel.getGraphics().clearRect(0, 0, mapDrawPanel.getWidth(), mapDrawPanel.getHeight());
            Vector<ArrayList<String>> aux = vc.getAllAddress();
            for(int i = 0; i < aux.size(); ++i) {
                String adress = aux.elementAt(i).get(1);
                int indexFinAv = 7;
                int indexInC = adress.indexOf("Carrer");
                int indexFinC = indexInC + 6;
                String d1 = adress.substring(indexFinAv+1, indexInC-1);
                String d2 = adress.substring(indexFinC);
                int nav = Integer.parseInt(d1)*50;
                int nc = Integer.parseInt(d2)*50;
                //System.out.println("nav: "+nav+" nc:"+nc);
                drawNode((Graphics2D)mapDrawPanel.getGraphics(),nav,nc,25,25);
            }
            Vector<String> aux2 = vc.getAddressWithPackets();
            for (int i = 0; i < aux2.size(); i++) {
                String adress = aux2.elementAt(i);
                int indexFinAv = 7;
                int indexInC = adress.indexOf("Carrer");
                int indexFinC = indexInC + 6;
                String d1 = adress.substring(indexFinAv+1, indexInC-1);
                String d2 = adress.substring(indexFinC);
                int nav = Integer.parseInt(d1)*50;
                int nc = Integer.parseInt(d2)*50;
                //System.out.println("nav: "+nav+" nc:"+nc);
                drawNodeWithPacket((Graphics2D)mapDrawPanel.getGraphics(),nav,nc,25,25);
            }
            Vector<String> aux3 = vc.getSolutionString();
            System.out.println(aux3.toString());    
            for (int i = 0; i < aux3.size()-1; i++) {
                    String adress = aux3.elementAt(i);
                    int indexFinAv = 7;
                    int indexInC = adress.indexOf("Carrer");
                    int indexFinC = indexInC + 6;
                    String av1 = adress.substring(indexFinAv+1, indexInC-1);
                    String c1 = adress.substring(indexFinC);
                    int nav = Integer.parseInt(av1)*50;
                    int nc = Integer.parseInt(c1)*50;
                    String adress2 = aux3.elementAt(i+1);
                    int indexFinAv2 = 7;
                    int indexInC2 = adress2.indexOf("Carrer");
                    int indexFinC2 = indexInC2 + 6;
                    String av2 = adress2.substring(indexFinAv2+1, indexInC2-1);
                    String c2 = adress2.substring(indexFinC2);
                    int nav2 = Integer.parseInt(av2)*50;
                    int nc2 = Integer.parseInt(c2)*50;
                    if(i == 0) mapDrawPanel.getGraphics().drawString("AV"+av1+"C"+c1, nav, nc+30);
                    mapDrawPanel.getGraphics().drawString("AV"+av2+"C"+c2, nav2, nc2+30);
                    drawLine((Graphics2D)mapDrawPanel.getGraphics(),nav+10,nc+10,nav2+10,nc2+10,Color.RED);
                    drawNodeSol((Graphics2D)mapDrawPanel.getGraphics(),nav+7,nc+7,10,10);
                    drawNodeSol((Graphics2D)mapDrawPanel.getGraphics(),nav2+7,nc2+7,10,10);
            }
        }
    }
}
