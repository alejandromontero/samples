package domain;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Clase RelationSet.
 * Esta clase gestiona todas las relaciones entre elementos.
 * 
 * @author Gerard Alonso
 * @version 1.1
 * @date 25/11/13
 */
public class RelationSet {
    
    /**
     * Tabla de Hash que contiene todas las relaciones entre los elementos.
     */
    protected HashMap<String, Integer> contRelacions;
    
    /**
    * Constructora de la classe RelationSet sin parametros.
    * Inicialitza el contenidor(hashmap).
    */
    public RelationSet() {
        contRelacions = new HashMap<String, Integer>();
    }
    
    /**
    * Constructora de la classe RelationSet con un parametro.
    * Inicialitza el contenidor(hashmap) a la pasada por parametro.
    * @param rs Un objeto de la clase RelationSet
    */
    public RelationSet(RelationSet rs) {
        contRelacions = rs.contRelacions;
    }
    
    /**
    * Método que transforma los dos ids de una relación a una string para
    * poder utilizarla como key del mapa de hash.
    * @param id1 El identificador del elemento 1.
    * @param id2 El identificador del elemento 2.
    * @return Una string con la key de la relación.
    */
    private String funcionHash(int id1, int id2) {
        String key;
        if(id1<id2) key = id1 + "-" + id2;
        else key = id2 + "-" + id1;
        return key; 
    }

    /**
    * Método que devuelve un objecto de tipo Relacion cuando se le especifican 
    * los dos ids de la relación.
    * @param id1 El identificador del elemento 1.
    * @param id2 El identificador del elemento 2.
    * @return Un objecto Relation. Si no existe la devuelve con los tres 
    * atributs(id1,id2,grado) a -1. 
    */  
    public Relation getRelation(int id1, int id2) {
        String key = funcionHash(id1,id2);
        if(contRelacions.containsKey(key)) {
            int grado = contRelacions.get(key);
            return new Relation(id1,id2,grado);
        }
        else return new Relation(-1,-1,-1);

    }
    
    /**
    * Método que devuelve el valor de una relación en concreto.
    * @param id1 El identificador del elemento 1.
    * @param id2 El identificador del elemento 2.
    * @return Un entero con el valor de la relación. Si no existe se retorna -1.
    */  
    public int getRelationValue(int id1, int id2) {
        String key = funcionHash(id1,id2);
        if(contRelacions.containsKey(key)) {
            return contRelacions.get(key);
        }
        else return -1;
    }
    
    /**
    * Método que inserta una relación al contenidor.
    * @param id1 El identificador del elemento 1.
    * @param id2 El identificador del elemento 2.
    * @param grado El grado de la relación.
    * @return Un booleano, true si se ha insertado, false en caso negativo.
    */
    public boolean addRelation(int id1, int id2, int grado) {
        String key = funcionHash(id1,id2);
        //Si existeix a la taula de hash, no s'afegeix
        if(contRelacions.containsKey(key)) return false;
        else {
             contRelacions.put(key,grado); 
        }
        return true;
    }
    
    /**
    * Método que elimina una relación del contenidor.
    * @param id1 El identificador del elemento 1.
    * @param id2 El identificador del elemento 2.
    * @return Un booleano, true si se ha borrado, false en cas negativo.
    */
    public boolean deleteRelation(int id1, int id2) {
        String key = funcionHash(id1,id2);
        //Si no existeix cap element a la taula de hash, no s'elimin
        if(!contRelacions.containsKey(key))return false; 
        else {
            contRelacions.remove(key);
        }
        return true;
    }
    
    
    /**
    * Método que modifica una relación del contenidor.
    * @param id1 El identificador del elemento 1.
    * @param id2 El identificador del elemento 2.
    * @param grado El nuevo grado de la relación.
    * @return Un boolea, true si se ha modificado, false en caso negativo.
    */
    public boolean modifyRelation(int id1, int id2, int grado) {
        String key = funcionHash(id1,id2);
        if(!contRelacions.containsKey(key)) return false;
        else {
            contRelacions.remove(key);
            contRelacions.put(key, grado);
        }
        return true;
    }
    
    /**
    * Método que lista todo el continenido del contenidor.
    * Para testear.
    */
    public void print() {
        for(Entry<String, Integer> entry: contRelacions.entrySet()) {
            System.out.println(entry.getKey().toString() + " -> " + entry.getValue());
            //System.out.println("and my hashcode is " + entry.hashCode());
        }
    }
    
    /**
    * Método que devuelve el tamaño del contenidor.
    * Para testear.
    * @return Un entero con el tamaño.
    */
    public int size() {
        return contRelacions.size();
    }
    
}
