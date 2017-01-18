package domain;

/**
 * Clase Address.
 * La Clase Address representa el contenido de una dirección.
 *
 * @author Gerard Alonso
 */
public class Address {

    /*Identificador de dirección*/
    private int id;
    /*Dirección*/
    private String ad;
    
    /**
     * Creadora sin parámetros.
     */
    public Address(){
       id = -1;
       ad = "";
    }
    
    /**
     * Creadora con parámetros. Inicializa el objeto con los parámetros pasados
     * por parámetro.
     * @param i Es el id del paquete.
     * @param a Es la nueva direccón.
     */
    public Address(int i,String a){
        id = i;
        ad = a;
    }
    
    /**
     * Método que devuelve el id de la dirección.
     * @return Un integer con el id de la dirección.
     */
    public int getId(){
        return id;
    }

    /**
     * Método que devuelve la dirección del paquete.
     * @return La dirección del paquete.
     */
    public String getAddress(){
        return ad;
    }
    
    /**
     * Método que modifica la dirección.
     * @param n Es la nueva dirección.
     */
    public void setAddress(String n){
        ad = n;
    }
    
    /**
     * Método que modifica la id de la dirección.
     * @param n Es el nuevo id.
     */
    public void setId(int n){
        id = n;
    }
}