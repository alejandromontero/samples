package domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Clase RelationSetExt
 * 
 * @author adria.manes.medina
 * @version 1.0
 * @date 25/11/13
 */
public class RelationSetExt extends RelationSet{
    
    
    public RelationSetExt(RelationSetExt e){
        super(e);
    }
    
    public RelationSetExt(){
        super();
    }
    
    /**
    * Método que
    * @return Un contenedor con todas las keys del hashMap de la clase.
    */
    public Set<String> getKeys() {
        return contRelacions.keySet();
    }
    
    /**
    * Método que devuelve el valor de la relación de la llave key
    * @param key La llave a buscar.
    * @return Un entero con el valor de la relación.
    */
    public Integer getRelationValue(String key) {
        if(contRelacions.containsKey(key)) {
            return contRelacions.get(key);
        }
        else return -1;
    }
    
    /**
    * Método que devuelve todas las relaciones.
    * @return Un contenedor(Vector de ArrayList de Strings) con todas las 
    * relaciones, con el formato: id1;id2;valor.
    */
    public Vector<ArrayList<String>> getAll(){
        Vector<ArrayList<String>> v = new Vector<>();
        String[] a;
        Set<Map.Entry<String,Integer>> ss = contRelacions.entrySet();
        Iterator<Map.Entry<String,Integer>> s = ss.iterator();
        while(s.hasNext()){
             Map.Entry<String,Integer> aux = s.next();
             ArrayList<String> element = new ArrayList<>();
             a = aux.getKey().split("-");
             element.add(a[0]);
             element.add(a[1]);
             element.add(Integer.toString(aux.getValue()));
             v.add(element);
        }
        return v;
  }
    
    /**
    * Método que devuelve todas las relaciones entre las direcciones especificadas.
    * @return Un contenedor(RelationSetExt) con todas las relaciones requeridas.
    */
    public RelationSetExt getRelationsFromAddress(ArrayList<Integer> ad){
        RelationSetExt rse2 = new RelationSetExt();
        for(int i=0; i<ad.size(); i++){
            for(int j=i+1; j<ad.size(); j++){
                int id1 = ad.get(i);
                int id2 = ad.get(j);
                int value = getRelationValue(id1,id2);
                if(value != -1) rse2.addRelation(id1, id2, value);
            }
        }
        return rse2;
    }
}
