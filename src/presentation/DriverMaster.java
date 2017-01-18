
package presentation;

import domain.DomainController;
import domain.RelationSetExt;
import exceptions.AlgorithmException;
import exceptions.FileException;
import exceptions.MapException;
import exceptions.PacketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerard
 */
public class DriverMaster {
    
    DomainController dc;
    
    public DriverMaster(){
        dc = new DomainController();
        try {
            dc.loadMap("C:/tmp/mapa50.txt");
            dc.loadPacketSet("C:/tmp/paquets20.txt");
        } catch (MapException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void main() {
     
      Scanner sc = new Scanner(System.in);
      int opt=1;
      int i,j,k;
      String x,y;
      
      while(opt!=0){
        menu();
        opt = sc.nextInt();
        switch(opt) {
          case 1: x = sc.next();
                  test_addPacket(x);
                  break;
          case 2: i = sc.nextInt();
                  x = sc.next();
                  test_modifyPacket(i,x);
                  break;       
          case 3: i = sc.nextInt();
                  test_deletePacket(i);
                  break;
          case 4: test_printPackets();
                  break;
          case 5: x = sc.next();
                  y = sc.next();
                  test_modifySolution(x,y);
                  break;    
          case 6: System.out.println("getSolutionInteger(): "+test_getSolutionInteger());
                  break;
          case 7: System.out.println("getSolutionString(): "+test_getSolutionString().toString());
                  break;
          case 8: System.out.println("getSolutionValue(): "+test_getSolutionValue());
                  break;
          case 9: test_printRelations();
                  break;
          case 10:  i = sc.nextInt();
                    j = sc.nextInt();
                    k = sc.nextInt();
                    test_modifyRelation(i,j,k);
                    break;
          case 11:  x = sc.next();
                    y = sc.next();
                    k = sc.nextInt();
                    test_modifyRelation2(x,y,k);
                    break;
          case 12: i = sc.nextInt();
                   test_runAlgorithm(i);
                  System.out.println("Resultat: "+dc.getSolutionInteger().toString()+"->"+dc.getSolutionValue());
                  break;
          case 13: i = sc.nextInt();
                   test_carrega(i);
                    break;
          }  
    }
    }
    
    public void menu(){
        System.out.println("MENU:\n" + "1.Addpacket(string ad)\n2.ModifyPacket(int id, string newad) \n3.DeletePacket(int id) \n4.PrintPackets");
        System.out.println("5.ModifySolution(string ad1,sting ad2) \n6.getSolutionInteger\n7.getSolutionString\n8.getSolutionValue");
        System.out.println("9.PrintRelations\n10.ModifyRelation(int id1,int id2, new value) \n11.same(String,String,int) \n12.RunAlgorithm(int mode)");
        System.out.println("13.Test_carrega\n0. EXIT");
    }
    
    private int test_addPacket(String a){
        try {
            return dc.addPacket(a);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PacketException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    private void test_deletePacket(int id){
        try {
            dc.deletePacket(id);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PacketException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void test_modifyPacket(int id, String a){
        try {
            dc.modifyPacket(id, a);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PacketException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void test_loadSolution(String filename){
        try {
            dc.loadSolution(filename);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void test_loadPacketSet(String filename){
        try {
            dc.loadPacketSet(filename);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void test_loadMap(String filename){
        try {
            dc.loadMap(filename);
        } catch (MapException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void test_saveSolution(String filename){
        try {
            dc.saveSolution(filename);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void test_modifySolution(String a1, String a2) {
        try {
            dc.modifySolution(a1, a2);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.toString());
        }
     }
    
     public Vector<ArrayList<String>> test_getAllPackets() {
         return dc.getAllPackets();
     }
     
     public Vector<ArrayList<String>> test_getAllAddress() {
         return dc.getAllAddress();
     }
     
     public Vector<ArrayList<String>> test_getAllRelations() {
         return dc.getAllRelations();
     }
     
     public Vector<Integer> test_getSolutionInteger() {
         return dc.getSolutionInteger();
     }
     
     public Vector<String> test_getSolutionString() {
         return dc.getSolutionString();
     }
     
     public Integer test_getSolutionValue() {
         return dc.getSolutionValue();
     }
     
     public void test_modifyRelation(Integer id1, Integer id2, Integer distancia){
        /*try {
            dc.modifyRelation(id1, id2, distancia);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }*/
     }
     
     public void test_modifyRelation2(String a1, String a2, Integer d){
        try {
            dc.modifyRelation(a1, a2, d);
        } catch (FileException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MapException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
  
     public void test_runAlgorithm(Integer mode) {
        try {
            dc.runAlgorithm(mode);
        } catch (AlgorithmException ex) {
            Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
      public RelationSetExt test_getRelationsWithPackets(){
          return dc.getRelationsWithPackets();
      }
      
      public ArrayList<Integer> test_getAllAddressWithPackets() {
          return dc.getAllAddressWithPackets();
      }

    private void test_printPackets() {
        Vector<ArrayList<String>> x = dc.getAllPackets();
        System.out.println("Hi ha "+x.size()+" paquets");
        for(int i=0;i<x.size();i++){
            System.out.println(x.get(i).get(0)+"-"+x.get(i).get(1));
        }
    }

    private void test_printRelations() {
        Vector<ArrayList<String>> x = dc.getAllRelations();
        for(int i=0;i<x.size();i++){
            System.out.println(x.get(i).get(0)+"-"+x.get(i).get(1)+"->"+x.get(i).get(2));
        }
    }

    private void test_carrega(int i) {
        char s = 'A';
        for(int j=0;j<i;j++){
            String aux = "" + s;
            try {
                dc.addPacket(aux);
            } catch (FileException ex) {
                Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PacketException ex) {
                Logger.getLogger(DriverMaster.class.getName()).log(Level.SEVERE, null, ex);
            }
            s++;
        }
        
    }
      
}
