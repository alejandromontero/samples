package domain;
import exceptions.AlgorithmException;
import java.util.*;

/**
 * Clase AlgorithmController
 * Clase encargada de gestionar los datos y el funcionamiento de las busquedas
 * iniciales y la optimizacion.
 * @author Alejandro
 */
public class AlgorithmController {
    
    /*Instancia de la clase Algorithm*/
    private Algorithm algorith;
    
    /*Instancia de la clase RelationSetExt*/
    private RelationSetExt rse;
    
    /*ArrayList que contiene las IDs de las direcciones con paquetes*/
    ArrayList<Integer> ids;
    
    /*Vector que contiene la solucion.*/
    private Vector<Integer> sol;
    
    /*Constructora*/
     public AlgorithmController(){
         algorith = new Algorithm();
         sol = new Vector<>();
     }
    
    /**
     * Metodo que inicializa los datos de la clase.
     * @param rse: Instancia de la clase RelationSetExt
     * @param ids: Lista que contiene los Ids de los nodos con paquetes. 
     */
    public void setData(RelationSetExt rse, ArrayList<Integer> ids){
         this.rse = rse;
         this.ids = ids;
         sol = new Vector<>();
    }
    /**
     * Metodo del controlador que ejecuta la busqueda de la solucion.
     * @param searchSelector: Parametro que permite seleccionar que busqueda inicial tomar.
     * @return: Se devuelve un vector con la solucion. 
     */
    public Vector<Integer> execute(int searchSelector) throws AlgorithmException {
        BeamSearch optimizer  = new BeamSearch(rse);
        if(ids.size() < 3) throw new AlgorithmException("No enough data to calculate");
        
        if (searchSelector == 1) {
            GreedySearch search = new GreedySearch(ids,rse);
            algorith = new Algorithm(search,optimizer);
        }
        else {
            Christofides search = new Christofides(ids,rse);
            algorith = new Algorithm(search,optimizer);
        }
        sol = algorith.searchSolution();
        return sol;
    }
}
