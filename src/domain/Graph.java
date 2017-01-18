
package domain;

import java.util.*;

/**
 *
 * @author adria.manes.medina
 */
public class Graph {

    private RelationSetExt RS;
    private int[] ID;
    
    /**
     * Constructora por defecto
     */
    public Graph(){
        RS = new RelationSetExt();
        
    }
    /**
     * Constructora con parametros de la clase graph
     * @param rel relationset 
     * @param id vector de ids
     */
    public Graph (RelationSetExt rel, int[] id){
        RS = rel;
        ID = id;
    }

    /** Método que comprueba si un grafo es completo.
     * @return Un booleano, true si el grafo es completo, false sino.
     */
    public boolean isComplete(){
        return RS.size() == ((ID.length * (ID.length-1))/2) ;
    }
    
    /** Método que devuelve el numero de nodos.
     * @return Un entero con el numero de nodos.
     */
    public Integer getnVertex() {
        return ID.length;
    }

    /** Método que devuelve todas las relaciones.
     * @return Un objeto con todas las relaciones.
     */
    public RelationSetExt getRelationSet() {
        return RS;
    }

    /**
     * @param RS the RS to set
     */
    public void setRelationSet(RelationSetExt RS) {
        this.RS = RS;
    }

    /**
     * @return the ID
     */
    public int[] getIdVector() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setIdVector(int[] ID) {
        this.ID = ID;
    }
    
    /**
     * 
     * @param id1
     * @param id2
     * @return relation value
     */
    public Integer getEdgeValue(Integer id1,Integer id2){
        return RS.getRelationValue(id1, id2); 
    }
    
    /**
     * Funcion que retorna todas las llaves del relation set.
     * @return Set de llaves.
     */
    public Set<String> getKeys(){
        return RS.getKeys();
    }
    
    /**
     * Función que retorna el valor de una relación dada una llave.
     * @param key
     * @return 
     */
    public Integer getRelationValue(String key) {
        return RS.getRelationValue(key);
    }
    
     /**
     * Método ...
     * @param id
     * @return Grado del vertice con el id del parametro.
     */
    public Integer getGradeVertex(Integer id){
        Integer contador = 0;
        Set<String> keys = RS.getKeys();
        
        for(int i = 0; i < ID.length; ++i){
            String key;
            key = funcionHash(id, (Integer)ID[i]);
            
            if(keys.contains(key)) {
                contador += 1;
            }
        }
        
        return contador;
    }
    
    /**
     * Método que devuelve la lista de nodes impares
     * @return Una LinkedList de nodos impares.
     */
    public LinkedList<Integer> getOddVertex(){
        LinkedList<Integer> odds = new LinkedList();
        for (int i = 0; i < ID.length; i++) {
            if(getGradeVertex(ID[i])%2==1) odds.add(ID[i]);
        }
        return odds;
    }
    
    /**
    * Método que transforma los dos ids de una relación en una string para
    * poderla utilitzar como una key de la taba/mapa de hash.
    * @param id1 El identificador del elemento 1.
    * @param id2 EL identificador del elemento 2.
    * @return un string con la key de la relación .
    */
    private String funcionHash(int id1, int id2) {
        String key;
        if(id1<id2) key = id1 + "-" + id2;
        else key = id2 + "-" + id1;
        return key; 
    }
    
    /**
     * Método que retorna un contenendor con todas las relaciones del grafo del
     * nodo con el id.
     * @param id Un entero con el id del nodo.
     * @return Retornamos un LinkedList de relaciones que contienen el id del 
     * parámetro.
     */
    public LinkedList<Relation> getRelationList(Integer id) {
        LinkedList<Relation> rel = new LinkedList();
        Set<String> keys = RS.getKeys();
        
        for (int i = 0; i < ID.length; ++i) {
            if(keys.contains(funcionHash(id,ID[i])) ){
                Integer value = this.getEdgeValue(id, ID[i]);
                Relation r = new Relation(id,ID[i],value);
                rel.add(r);
            }
        }
        return rel;
    }
}