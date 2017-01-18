
package domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;
import java.util.Random;

/**
 *
 * @author adria.manes.medina
 */
public class MultiGraph {
    private HashMap<String, Vector<Integer> > mult;
    private Vector<Integer> ID;
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
     * Constructor por defecto
     */
    public MultiGraph(){
        mult = new HashMap();
        ID = new Vector();
    }
    
    /**
     * Constructora de un multigrafo a partir de dos grafos
     * @param g1
     * @param g2 
     */
    public MultiGraph(Graph g1, Graph g2){
        mult = new HashMap();
        Set<String> keysG1, keysG2;
        keysG1 = g1.getKeys();
        keysG2 = g2.getKeys();
        Integer value;
        Vector<Integer> aux;
        
        Iterator<String> iterator = keysG1.iterator();
        Iterator<String> iterator2 = keysG2.iterator();
        while(iterator.hasNext()) {
            String key = iterator.next();
            value = g1.getRelationValue(key);
            if(!mult.containsKey(key)){
                aux = new Vector();
                aux.add(value);
                mult.put(key, aux);
            }
            else{
                aux = mult.get(key);
                aux.add(value);
                mult.remove(key);
                mult.put(key, aux);
            }
        }
        
        
        while(iterator2.hasNext()) {
            String key = iterator2.next();
            value = g2.getRelationValue(key);
            if(!mult.containsKey(key)){
                aux = new Vector();
                aux.add(value);
                mult.put(key, aux);
            }
            else{
                aux = mult.get(key);
                aux.add(value);
                mult.remove(key);
                mult.put(key, aux);
            }
        }
        
        ID = setIdVector(g1.getIdVector(),g2.getIdVector());
    }
    
    /**
     * Procedimiento para añadir una arista al multigrafo
     * @param id1
     * @param id2
     * @param value 
     */
    public void addEdge(Integer id1, Integer id2, Integer value){
        String key = funcionHash(id1,id2);
        Vector<Integer> aux;
        
        if(!mult.containsKey(key)){
            aux = new Vector();
            aux.add(value);
            mult.put(key, aux);
        }
        else{
            aux = mult.get(key);
            aux.add(value);
            mult.remove(key);
            mult.put(key, aux);
        }
    }
    
    /**
     * Procedimiento para borrar una aresta del multigrafo
     * @param id1
     * @param id2
     * @param value 
     */
    public void delEdge(Integer id1, Integer id2, Integer value){
        String key = funcionHash(id1,id2);
        Vector<Integer> aux;
        
        if(mult.containsKey(key)){
            aux = mult.get(key);
            aux.remove(value);
            mult.remove(key);
            mult.put(key, aux);
        }
    }
    
    /**
     * Función para obtener los pesos de las arestas entre dos nodos
     * @param id1
     * @param id2
     * @return vector de pesos entre los nodos con id1 e id2
     */
    public Vector<Integer> getEdges(Integer id1, Integer id2){
        String key = funcionHash(id1,id2);
        if(mult.containsKey(key)) return mult.get(key);
        else return new Vector();
    }
    
    /**
     * 
     * @param v1
     * @param v2
     * @return Vector de la union de los dos arrays.
     */
    public Vector<Integer> setIdVector(int[] v1, int[] v2){
        Vector<Integer> aux = new Vector();
        
        for(int j = 0; j < v1.length; ++j) aux.addElement(v1[j]);
        
        for(int i = 0; i < v2.length; ++i){
            if (!aux.contains(v2[i])) aux.addElement(v2[i]);
        }
        
        return aux;
    }
    
    /**
     * 
     * @return Devuelve un id random contenido en nuestro vector de ids
     */
    public Integer getRandomVertex(){
        Random r = new Random();
        return ID.get(r.nextInt(ID.size()-1));  
    }
    
    /**
     * 
     * @param id
     * @return Grado del vertice con el id del parametro.
     */
    public Integer getGradeVertex(Integer id){
        Integer contador = 0;
        
        for(int i = 0; i < ID.size(); ++i){
            String key = funcionHash(id,ID.get(i));
            if(mult.containsKey(key)) {
                contador += mult.get(key).size();
            }
        }
        
        return contador;
    }
    
    /**
     * 
     * @return true si el multigrafo es Euleriano
     */
    public Boolean isEulerian(){
        Boolean isEulerian = true;
        
        for(int i = 0; i < ID.size(); ++i) {
            isEulerian = (getGradeVertex(ID.get(i))%2 == 0);
            if(!isEulerian) return false;
        }
        
        return isEulerian;
    }
    
    /**
     * 
     * @param id
     * @return Vector de ids
     * Dado un id devuelve un vector de ids de nodos adyacentes
     */
    public Vector<Integer> getVectorVertex(Integer id){
        Vector<Integer> aux = new Vector();
        
        for(int i = 0; i < ID.size(); ++i){
            String key = funcionHash(id,ID.get(i));
            if(mult.containsKey(key)) {
                aux.add(ID.get(i));
            }
        }
        
        return aux;
    }
    
    /**
     * Este procedimiento escribe las claves y su contenido de todo el multigrafo
     */
    public void printMultiGraph(){
        Set keys = mult.keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()) {
            String s = iterator.next();
            System.out.print(s+" ");
            Vector<Integer> aux = mult.get(s);
            for (int i = 0; i < aux.size(); ++i) System.out.print(aux.get(i)+" ");
            System.out.print("\n");
        }
    }

    /**
     * @return the ID
     */
    public Vector<Integer> getIdVector() {
        return ID;
    }
    
    /**
     * 
     * @param L 
     * Dada una lista L borramos todas las arestas de ese subconjunto de ids
     * también borramos los ids de nodos que quedan sin ninguna aresta
     * con lo que borramos ese nodo.
     */
    public void delEdges(LinkedList<Integer> L){
        for (int i = 0; i < L.size(); ++i) {
            for (int j = 0; j < L.size(); ++j) {
                String key = funcionHash(L.get(i),L.get(j));
                if(mult.containsKey(key)) mult.remove(key);
            }
        }
        
        for (int i = 0; i < L.size(); ++i) {
            Boolean isIn = false;
            for (int j = 0; j < ID.size() &&  !isIn; j++) {
                if(mult.containsKey(funcionHash(L.get(i),ID.get(j)))) isIn = true;
            }
            if(!isIn) ID.remove(L.get(i));
            
        }
    }
    
}