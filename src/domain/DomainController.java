package domain;

import exceptions.*;
import java.util.ArrayList;
import java.util.Vector;
import persistence.FileManagerController;

/**
 * Clase DomainController.
 * La Clase FileManagerController se encarga de gestionar las estructuras de 
 * datos AddressSet, RelationSetExt, PacketSet, SolutionExt y utilizando el 
 * file manager y dando soporte al view controller.
 *
 * @author Gerard Alonso
 */
public class DomainController {
    
    private PacketSet ps;
    private AddressSet as;
    private RelationSetExt rse;
    private SolutionExt solution;
    private final FileManagerController fmc;
    
    //ruta on esta carregada la solucio i mapa actual
    private String solutionFile;
    private String mapFile;
    private String packetsFile;
    
    private final AlgorithmController ac;
    
    /**
    * Constructora por defecto.
    * Crea una instancia de DomainController.
    */
    public DomainController() {
        ps = new PacketSet();
        as = new AddressSet();
        rse = new RelationSetExt();
        solution = new SolutionExt();
        fmc = new FileManagerController();
        ac = new AlgorithmController();
    }
    
    /**
    * Constructora con parametros.
    * Crea una instancia de DomainController inicializandola con sus datos.
    * @param r Las relaciones.
    * @param p La lista de paquetes.
    * @param a La lista de direcciones.
    */
    public DomainController(RelationSetExt r, PacketSet p, AddressSet a) {
        ps = p;
        as = a;
        rse = r;
        solution = new SolutionExt();
        fmc = new FileManagerController();
        ac = new AlgorithmController();
    }
	
	
    /**
    * Método para cambiar la ruta del fichero de solución cargado.
    * @param sf Es la ruta del fichero de solución cargado.
    */
    public void setSolutionFile(String sf) {
        this.solutionFile = sf;
    }

    /**
    * Método para cambiar la ruta del fichero de Mapa.
    * @param mf Es la ruta del fichero de mapa cargado.
    */
    public void setMapFile(String mf) {
        this.mapFile = mf;
    }

    /**
    * Método para cambiar la ruta del fichero de Paquetes.
    * @param pf Es la ruta del fichero de paquetes cargado.
    */
    public void setPacketsFile(String pf) {
        this.packetsFile = pf;
    }
    

    /*FILE MANAGER METHODS*/
    
    /**
    * Método que actualiza la solución al fichero.
    * @throws FileException Cuando hay algun error a la hora de actualizar el 
    * fichero con la nueva información.
    */
    private void updateSolution() throws FileException{
        //S'ha de controlar si s'ha carregat un fitxer o no
        if(solutionFile != null){
            //conversion a un vector de strings
            Vector<String> aux = new Vector<>();
            for(int i=0;i<solution.getSolution().size();i++){
                aux.add(as.getAddress(solution.getSolution().get(i)).getAddress());
            }
            fmc.saveSolution(solutionFile, aux);
        }
    }
    
   /**
    * Método que actualiza el mapa si se ha realizado alguna modificación a alguna
    * relación entre direcciones.
    * @throws FileException Cuando hay algun error a la hora de actualizar el 
    * fichero con la nueva información.
    */
    private void updateMap() throws FileException{
        //S'ha de controlar si s'ha carregat un fitxer o no
        if(mapFile != null){
            fmc.saveMaps(mapFile, as.getAllString(), getAllRelationsAddress());
        }
    }
    
    /**
    * Método que actualiza el mapa si se ha realizado alguna modificación a alguna
    * relación entre direcciones.
    * @throws FileException Cuando hay algun error a la hora de actualizar el 
    * fichero con la nueva información.
    */
    private void updatePacketSet() throws FileException{
        //S'ha de controlar si s'ha carregat un fitxer o no
        if(packetsFile != null) {
            fmc.savePackets(packetsFile, ps.getAllAddressString());
        }
    }
    
    /**
    * Método que carga una solución desde un fichero.
    * @param filename Nombre del fichero origen. En cada linea una dirección.
    * @throws FileException Cuando hay algun error a la hora de cargar el 
    * fichero con la nueva información.
    */
    public void loadSolution(String filename) throws FileException {
        fmc.loadPacketOrSolution(filename);
        solutionFile = filename;
    }
    
    /**
    * Método que carga un conjunto de paquetes.
    * @param filename Nombre del fichero origen. En cada linea una dirección de
    * paquete.
    * @throws FileException Cuando hay algun error a la hora de cargar el 
    * fichero con la nueva información.
    */
    public void loadPacketSet(String filename) throws FileException {
        Vector<String> x = fmc.loadPacketOrSolution(filename);
        ps = new PacketSet();
        for(int i=0;i<x.size();i++){
            Address a = as.getAddress(x.get(i));
            if(a == null) throw new FileException("Incorrect address");
            ps.addPacket(a);
        }
        packetsFile = filename;
        solution = new SolutionExt();
    }
    
    /**
    * Método que carga todo el mapa desde un fichero.
    * @param filename Nombre del fichero origen para cargarlo. Este fichero 
    * tiene que contener primero las direcciones(una en cada linea), luego un #
    * y despues las relaciones entre las direcciones(dirección1;dirección2;dist)
    * El mapa tiene que ser completo, tienen que existir todas las relaciones.
    * @throws MapException Cuando el fichero que contiene el mapa no
    * cumple con los requisitos de formato y/o contenido.
    * @throws FileException Cuando hay algun error a la hora de
    * cargar un fichero con la nueva información.
    */
    public void loadMap(String filename) throws MapException, FileException {
        Vector<String> x = fmc.loadAddress(filename);
        Vector<ArrayList<String>> x2 = fmc.loadRelations(filename);
        
        if(x.size() < 3) throw new MapException("Not enough data");
        if(!(x2.size() == ((x.size()) * (x.size()-1)) / 2)) throw new MapException("Invalid file format");
        
        AddressSet aux_as = new AddressSet();
        RelationSetExt aux_rse = new RelationSetExt();
        
        for(int i=0;i<x.size();i++){
            aux_as.addAddress(x.get(i));
        }
        for(int i=0;i<x2.size();i++){
            int id1,id2,valor;
            ArrayList<String> aux = x2.get(i);
            Address a1 = aux_as.getAddress(aux.get(0));
            if(a1 != null) id1 = a1.getId();
            else id1 = -1;
            Address a2 = aux_as.getAddress(aux.get(1));
            if(a2 != null) id2 = a2.getId();
            else id2 = -1;
            valor = Integer.parseInt(aux.get(2));
            if(id1 == id2 || id1 < 0 || id2 < 0 || valor < 0) throw new MapException("Incorrect relations");
            else aux_rse.addRelation(id1, id2, valor);
        }
        
        as = aux_as;
        rse = aux_rse;
        //guardem ruta fitxer
        mapFile = filename;
        //inicialitzem la llista de paquets i la solució
        ps = new PacketSet();
        solution = new SolutionExt();
    }
    
    /**
    * Método que guarda la solución desde el contenedor RelationSetExt a el fichero
    * especificado.
    * @param filename Nombre del fichero destino (se sobreescribirá).
    * @throws FileException Cuando hay algun error a la hora de
    * salvar el fichero con la nueva información.
    */
    public void saveSolution(String filename) throws FileException {
       Vector<String> aux = new Vector<>();
       if(solution.getSolution().isEmpty()) throw new FileException("No solution found");
       for(int i=0;i<solution.getSolution().size();i++){
            aux.add(as.getAddress(solution.getSolution().get(i)).getAddress());
       }
       fmc.saveSolution(filename, aux);
       solutionFile = filename;
    }
    
    /**
    * Método que modifica la solución intercanviando las dos direcciones pasadas
    * por parametro. Actualiza solo el sistema, no toca ningun fichero.
    * @param a1 Un string con la primera dirección.
    * @param a2 Un string con la segunda dirección.
     * @throws FileException Cuando se introducen direcciones incorrectas.
    */
    public void modifySolution(String a1, String a2) throws FileException {
       int pa1,pa2;
       pa1 = pa2 = -1;
       Integer id1 = as.getAddress(a1).getId();
       Integer id2 = as.getAddress(a2).getId();
       if(a1.equals(a2)) throw new FileException("Wrong address(same address)");
       for(int i=0;i < solution.getSolution().size() && (pa1 == -1 || pa2 == -1);i++){
           Integer x = solution.getSolution().get(i);
           if(x.equals(id1)) pa1 = i;
           else if(x.equals(id2)) pa2 = i;
       }
       if(pa1 == -1 || pa2 == -1) throw new FileException("Wrong address");
       solution.getSolution().set(pa1, id2);
       solution.getSolution().set(pa2, id1);
       //Calcular nuevo valor de solucion
       int valor=0;
       for(int i=0;i<solution.getSolution().size()-1;i++){
            valor += rse.getRelationValue(solution.getSolution().get(i), solution.getSolution().get(i+1));
       }
       valor += rse.getRelationValue(solution.getSolution().get(solution.getSolution().size()-1), solution.getSolution().get(0));
       solution.setValue(valor);

    }
    
    /*PRESENTATION CONTROLLER METHODS*/
    
    /**
    * Método que obtiene todos los paquetes(con su id y su dirección).
    * @return Un vector de ArrayList de Strings que contiene los paquetes.
    */ 
    public Vector<ArrayList<String>> getAllPackets() {
        return ps.getAllPackets(); 
    }
    
    /**
    * Método que obtiene todas las Address (con su id y dirección).
    * @return Un vector de ArrayList que contiene la información de las direcciones
    * id y dirección.
    */
    public Vector<ArrayList<String>> getAllAddress() {
        return as.getAll();
    }
    
    /**
    * Método que obtiene las relaciones (con sus dos ids y su valor).
    * @return Un vector de ArrayList(un por cada relación). En cada ArrayList 
    * esta estructurado id1,id2 y valor.
    */
    public Vector<ArrayList<String>> getAllRelations() {
        return rse.getAll();
    }
    
    /**
     * Método que retorna todas las relaciones con las direcciones y el valor.
     * @return Un vector de ArrayList(un por cada relación). En cada ArrayList 
     * esta estructurado Addres1,Addres2 y valor.
     */
    public Vector<ArrayList<String>> getAllRelationsAddress() {
        Vector<ArrayList<String>> aux = rse.getAll();
        for (int i = 0; i < aux.size(); i++) {
                aux.elementAt(i).set(0, as.getAddress(Integer.parseInt(aux.elementAt(i).get(0))).getAddress());
                aux.elementAt(i).set(1, as.getAddress(Integer.parseInt(aux.elementAt(i).get(1))).getAddress());
        }
        return aux;
    }
    
    
    /**
    * Método que obtiene la solución.
    * @return Un vector que contiene los ids de las direcciones de la solución.
    */
    public Vector<Integer> getSolutionInteger() {
        return solution.getSolution();
    }
    
    /**
    * Método que obtiene la solución con el nombre de las direcciones.
    * @return Un vector que contiene las direcciones de la solución.
    */
    public Vector<String> getSolutionString() {
        Vector<Integer> aux = solution.getSolution();
        Vector<String> r = new Vector<>();
        for(int i=0;i<aux.size();i++){
            r.add(as.getAddress(aux.elementAt(i)).getAddress());
        }
        return r;
    }
    
    /**
    * Método que obtiene el valor de la solución.
    * @return Un entero con la distancia de la solución.
    */
    public Integer getSolutionValue() {
       return solution.getValue();
    }
    
    
    /**
    * Método que elimina el paquete especificado su id.
    * @param id Id del paquete a eliminar.
    * @throws FileException Cuando hay algun error a la hora de actualizar el 
    * fichero con la nueva información.
    * @throws PacketException Cuando el paquete no ha podido ser borrado porque
    * no existe.
    */
    public void deletePacket(Integer id) throws FileException, PacketException {
       if(!ps.removePacket(id)) throw new PacketException("The packet doesn't exist");
       updatePacketSet();
    }
    
    /**
    * Método que añade un nuevo paquete.
    * @param a Dirección del paquete a añadir.
    * @return El id del nuevo paquete o -1(si no se ha insertado). 
    * @throws FileException Cuando hay algun error a la hora de actualizar el 
    * fichero con la nueva información.
    * @throws PacketException Cuando la dirección del paquete no es correcta.
    */
    public int addPacket(String a) throws FileException, PacketException {       
        int r = -1;
        Address x = as.getAddress(a);
        if(x != null) {
            r = ps.addPacket(x);
            updatePacketSet();
        }
        else{
            throw new PacketException("Incorrect address");
        }
        return r;
    }
    
    /**
    * Método que modifica la dirección de un paquete.
    * @param id Id del paquete a modificar.
    * @param a Dirección del paquete a modificar.
    * @throws FileException Cuando hay algun error a la hora de
    * actualizar el fichero con la nueva información.
    * @throws PacketException Cuando se intenta modificar un paquete con una
    * dirección no valida.
    */
    public void modifyPacket(int id, String a) throws FileException, PacketException {
        Address x = as.getAddress(a);
        if(x != null) { 
            if(!ps.modifyPacket(id, x)) throw new PacketException("Incorrect id");
            updatePacketSet();
        }
        else{
            throw new PacketException("Incorrect address");
        }
    }

    /**
    * Método que modifica la distancia de una relación.
    * @param ad1 Dirección del primer elemento.
    * @param ad2 Dirección del segundo elemento.
    * @param distancia Nuevo valor de a relación.
    * @throws FileException Cuando hay algun error a la hora de actualizar el 
    * fichero con la nueva información.
    */
    public void modifyRelation(String ad1, String ad2, Integer distancia) throws FileException, MapException{
        Integer a = as.getAddress(ad1).getId();
        Integer b = as.getAddress(ad2).getId();
        if(a == null || b == null) throw new MapException("Wrong address");
        rse.modifyRelation(a,b,distancia);
        updateMap(); //actualiza fichero
    }
    
    
    /*ALGORITHM CONTROLLER METHODS*/
    
    /**
    * Método que activa el algoritmo resolver.
    * Ejecuta los algoritmos correspondientes al modo y calcula una solucion.
    * @param mode Define la cambinacion de algoritmos a usar.
    * @throws AlgorithmException Cuando la ejecución no es correcta.
    */
    public void runAlgorithm(Integer mode) throws AlgorithmException {
       ArrayList<Integer> aux = new ArrayList<>(ps.getAllAddressIdWithPacket());
       RelationSetExt aux2 = new RelationSetExt(rse.getRelationsFromAddress(aux));
       ac.setData(aux2,aux);
       Vector<Integer> s = null;
       s = ac.execute(mode);
       int valor=0;
       for(int i=0;i<s.size()-1;i++){
            valor += rse.getRelationValue(s.get(i), s.get(i+1));
       }
       valor += rse.getRelationValue(s.get(s.size()-1), s.get(0)); 
       solution = new SolutionExt(s,valor);
    }
    
    /**
    * Método que devuelve un contenedor con las relaciones entre direcciones con
    * paquetes.
    * @return Un RelationSetExt solo con las relaciones entre direcciones
    * que tiene paquete.
    */
    public RelationSetExt getRelationsWithPackets(){
        return rse.getRelationsFromAddress(ps.getAllAddressIdWithPacket());
    }
    
    /**
    * Método que obtiene todos los ids de las direcciones con paquete.
    * @return Un ArrayList de Integer que contiene los ids.
    */ 
    public ArrayList<Integer> getAllAddressWithPackets() {
        return ps.getAllAddressIdWithPacket();
    }
    
    /**
    * Método que obtiene todos las direcciones con paquete.
    * @return Un Vector de String que contiene las direcciones.
    */ 
    public Vector<String> getAllAddressWithPacketsString() {
        Vector<Address> va = ps.getAllAddressWithPacket();
        Vector<String> vr = new Vector<>();
        for(int i=0;i<va.size();i++){
            vr.add(va.get(i).getAddress());
        }
        return vr;
    }
}