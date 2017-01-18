
package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

/**
 * Clase AddressSet.
 * La Clase AddressSet se encarga de gestionar las direcciones del mapa.
 *
 * @author Gerard Alonso
 */
public class AddressSet {
    
    /*El contenedor de direcciones*/
    HashMap<Integer,Address> as;
    /*El id que se ira assignado a las direcciones*/
    int contador;
    
    /*Creadora por defecto. Inicializa las estrucutras de datos.*/
    public AddressSet(){
        as = new HashMap<>();
        contador = 0;
    }
    
    /**
    * Método que devuelve una dirección en concreto.
    * @param id Un identificador de dirección.
    * @return El objeto Address(dirección).
    */
    public Address getAddress(int id){
        return as.get(id);
    }
    
    /**
    * Método que devuelve una dirección en concreto.
    * @param a Una dirección.
    * @return El objeto Address(dirección).
    */
    public Address getAddress(String a){
        Vector<String> v = new Vector<>();
        Set<Entry<Integer,Address>> ss = as.entrySet();
        Boolean trobat = false;
        Iterator<Entry<Integer,Address>> s = ss.iterator();
        while(s.hasNext() && !trobat){
             Entry<Integer, Address> au = s.next();
             if(au.getValue().getAddress().equals(a)) return au.getValue();
        }
        return null;
    }
    
    /**
    * Método que añade una nueva dirección.
    * @param a El objeto Address a añadir.
    */
    public void addAddress(String a){
        Address add = new Address(contador,a);
        as.put(contador, add);
        contador++;
    }
    
    /*NOT USED*/
    /**
    * Método que elimina una dirección en concreto.
    * @param int Un identificador de dirección.
    * @return Un booleano si elimina o no dicha dirección.
    */
    public boolean deleteAddress(int id){
        if(as.remove(id) == null)return false;
        else return true;
    }

    /*NOT USED*/
    /**
    * Método que modifica una dirección en concreto.
    * @param id Un identificador de dirección.
    * @param nouValor Una String con la nueva dirección.
    */
    public void modifiyAddress(int id, String nouValor){
        if(as.containsKey(id)){
            Address aux = as.get(id);
            aux.setAddress(nouValor);
        }
    }
    
    /**
    * Método que devuelve todas las direcciones.
    * @return Un contenedor(Vector de ArrayList de Strings) con todas las direcciones
    * preparadas para la capa de presentación.
    */
    public Vector<ArrayList<String>> getAll(){
        Vector<ArrayList<String>> v = new Vector<>();
        Address a = new Address();
        Set<Entry<Integer,Address>> ss = as.entrySet();
        Iterator<Entry<Integer,Address>> s = ss.iterator();
        while(s.hasNext()){
             Entry<Integer, Address> aux = s.next();
             ArrayList<String> element = new ArrayList<>();
             element.add(Integer.toString(aux.getValue().getId()));
             element.add(aux.getValue().getAddress());
             v.add(element);
        }
        return v;
  }
    
    /**
    * Método que devuelve todas las direcciones(sin el id).
    * @return Un contenedor(Vector de Strings) con todas las direcciones preparadas
    * para la capa de persistencia.
    */
    public Vector<String> getAllString(){
        Vector<String> v = new Vector<>();
        Address a = new Address();
        Set<Entry<Integer,Address>> ss = as.entrySet();
        Iterator<Entry<Integer,Address>> s = ss.iterator();
        while(s.hasNext()){
             Entry<Integer, Address> aux = s.next();
             v.add(aux.getValue().getAddress());
        }
        return v;
  }

}
