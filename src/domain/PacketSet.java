
package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author adria.manes.medina
 */
public class PacketSet {
    
    /** Atributo para asignar id a paquetes. */
    private Integer counter;
    
    /*ids reciclados de direcciones borradas*/
    PriorityQueue<Integer> deletedIDs;
    
    /* Contenedor */
    private final HashMap<Integer, Packet> packets;

    /* Direcciones con paquete */
    private HashSet<Address> as;
    
    /**
     * Creadora por defecto.
     */
    public PacketSet() {
       counter = 1;
       packets = new HashMap();
       as = new HashSet<>();
       deletedIDs = new PriorityQueue<>();
    }
    
    /**
     * Método que añade un paquete.
     * @param a Un objeto Address con la dirección del paquete.
     * @return Un Boolean que devuelve si se ha introducido o no.
     */
    public Integer addPacket(Address a) {
        Integer r = -1;
        if(!as.contains(a)){
            as.add(a); //afegim direcció
        }
        if(!deletedIDs.isEmpty()){
            r = deletedIDs.remove();
            Packet aux = new Packet(r,a);
            packets.put(r, aux); //afegim paquet
        }
        else{
           Packet aux = new Packet(counter,a);
            packets.put(counter, aux); //afegim paquet
            r = counter;
            ++counter; 
        }
        
        return r;
    }
    
    /**
     * Método que elimina un paquete en concreto.
     * @param id Es un id de paquete.
     * @return Un Boolean que devuelve si se ha eliminado o no.
     */
    public Boolean removePacket(Integer id){
        Packet remove = packets.remove(id);
        if(remove != null) {
            as.remove(remove.getAddress());
            recalculateAS();
            deletedIDs.add(id);
            return true;
        }
        return false;
    }
    
    /**
     * Método que modifica una dirección de un paquete en concreto.
     * @param id  Es el id del paquete.
     * @param ad Es la nueva dirección
     * @return Un Boolean que devuelve si se ha modificado o no.     
     */
    public Boolean modifyPacket(Integer id, Address ad){
        if(packets.containsKey(id)){ 
            Packet p = packets.get(id);
            p.setAddress(ad);
            packets.put(id, p);
            recalculateAS();
            return true;
        }
        return false;
    }
    
    /**
     * Método que devuelve un paquete.
     * @param id Es el id del paquete.
     * @return Devuelve un objeto Packet con el id especificado.
     */
    public Packet getPacket(Integer id) {
        return packets.get(id);
    }
    
    /**
     * Método que devuelve la dirección del paquete especificado.
     * @param id Es el id del paquete.
     * @return Devuelve un objeto Address con la dirección del paquete.
     */
    public Address getAddress(Integer id) {
        Packet aux = packets.get(id);
        if (aux != null) return aux.getAddress();
        else return null;
    }
    
    /**
    * Método que devuelve el conjunto de direcciones con paquete (sin repeticiones).
    * @return Devuelve un contenedor(vector de Strings) con todas las direcciones
    * de los paquetes (sin repeticiones).
    */
    public Vector<String> getAllAddressString(){
        Vector<String> v = new Vector<>();
        Address a = new Address();
        Set<Map.Entry<Integer,Packet>> ss = packets.entrySet();
        Iterator<Map.Entry<Integer,Packet>> s = ss.iterator();
        while(s.hasNext()){
             Map.Entry<Integer, Packet> aux = s.next();
             String ad = aux.getValue().getAddress().getAddress();
             if(!v.contains(ad)) v.add(ad);
        }
        return v;
    }
    
        
    /**
    * Método que devuelve todos los paquetes con toda la información(id y dirección).
    * @return Devuelve un contenedor(Vector de ArrayList de String) con todos 
    * los paquetes
    */
     public Vector<ArrayList<String>> getAllPackets(){
        Vector<ArrayList<String>> v = new Vector<>();
        Iterator<Map.Entry<Integer,Packet>> s = packets.entrySet().iterator();
        while(s.hasNext()){
             Map.Entry<Integer, Packet> aux = s.next();
             ArrayList<String> x = new ArrayList<>();
             if(aux.getValue().getAddress() != null){
             x.add(Integer.toString(aux.getValue().getId()));
             x.add(aux.getValue().getAddress().getAddress());
             v.add(x);}
        }
        return v;
     }

    /**
    * Método que actualiza el contenedor de direcciones únicas.
    */
    private void recalculateAS() {
        HashSet<Address> tmp = new HashSet<>();
        Set<Map.Entry<Integer,Packet>> ss = packets.entrySet();
        Iterator<Map.Entry<Integer,Packet>> s = ss.iterator();
        while(s.hasNext()){
             Map.Entry<Integer, Packet> aux = s.next();
             if(!tmp.contains(aux.getValue().getAddress())){
                 tmp.add(aux.getValue().getAddress());
             }
        }
       as = tmp;
    }

    /**
    * Método que devuelve todas las direcciones que contienen paquetes sin repetidos.
    * @return Devuelve un contenedor(Vector de Address) con todas las direcciones 
    * de los paquetes
    */
    public Vector<Address> getAllAddressWithPacket(){
        Vector<Address> v = new Vector<>();
        Iterator<Address> it = as.iterator();
        while(it.hasNext()){
            v.add(it.next());
        }
        return v;
    }
    
    /**
    * Método que devuelve todas las ids de las direcciones que contienen paquetes 
    * sin repetidos.
    * @return Devuelve un contenedor(ArrayList de Integers) con todas las ids de
    * direcciones de los paquetes.
    */
    public ArrayList<Integer> getAllAddressIdWithPacket(){
        ArrayList<Integer> v = new ArrayList<>();
        Iterator<Address> it = as.iterator();
        while(it.hasNext()){
            v.add(it.next().getId());
        }
        return v;
    }
}