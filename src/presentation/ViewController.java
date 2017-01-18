
package presentation;
import exceptions.*;
import domain.DomainController;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * @author naesh619
 */
public class ViewController{
    
    private DomainController dc;
    
    /**
     * @param dc 
     */
    public ViewController(){
        dc = new DomainController();
        
    }
    
    /**
     * 
     * @param address
     * @return 
     */
    public Integer addPacket(String address){
        try {
            return dc.addPacket(address);
        } catch (FileException|PacketException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        }
        return null;
    }
    
    public void deletePacket(Integer id) {
        try {
            dc.deletePacket(id);
        } catch (FileException|PacketException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        }
    }
    
    public void setPacket(Integer id, String address){
        try {
            dc.modifyPacket(id, address);
        } catch (FileException|PacketException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        }
    }
    
    public void loadPacketSet() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new MyFileFilter());
            chooser.setDialogTitle("Select the packet set");
            chooser.showOpenDialog(null);
            File f = chooser.getSelectedFile();
            String filename = f.getAbsolutePath();
            dc.loadPacketSet(filename);
        } catch (FileException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        }
    }
    
    public void loadMap() {
        try {
            
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new MyFileFilter());
            chooser.setDialogTitle("Select the map");
            chooser.showOpenDialog(null);
            File f = chooser.getSelectedFile();
            String filename = f.getAbsolutePath();
            dc.loadMap(filename);
        } catch (MapException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        } catch (FileException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        }

    }
    
    public void modifyRelation(String id1, String id2, Integer distancia) {
        try {
            dc.modifyRelation(id1, id2, distancia);
        } catch (FileException|MapException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        }
    }

    
    public void runAlgorithm(Integer mode) {
        try {
            dc.runAlgorithm(mode);
        } catch (AlgorithmException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        }
    }
    
    public Vector<Integer> getSolutionInteger() {
        return dc.getSolutionInteger();
    }
    
    public Vector<String> getSolutionString() {
        return dc.getSolutionString();
    }
    
    public void modifySolution(String a1,String a2){
        try {
            dc.modifySolution(a1,a2);
        } catch (FileException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
            
        }
    }
    
    public void saveSolution() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new MyFileFilter());
            chooser.setDialogTitle("Select file to save");
            chooser.showOpenDialog(null);
            File f = chooser.getSelectedFile();
            String filename = f.getAbsolutePath();
            dc.saveSolution(filename);
        } catch (FileException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        }
    }
    
    
    
    
    public Vector<ArrayList<String>> getAllAddress() {
        return dc.getAllAddress();
    }
    
    public Vector<ArrayList<String>> getAllRelations() {
        return dc.getAllRelationsAddress();
    }
    
    
    
    public Vector<ArrayList<String>> getAllPackets() {
        return dc.getAllAddress();
    }
    
    public void loadSolution(String filename) {
        try {
            dc.loadSolution(filename);
        } catch (FileException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "ERROR:", JOptionPane.OK_CANCEL_OPTION);
        }
    }
    
    public Vector<String> getAddressWithPackets(){
        return dc.getAllAddressWithPacketsString();
    }
   
}
