
package domain;

import java.util.Scanner;

/**
 *
 * @author adria.manes.medina
 */
public class Packet {
    /**
     * id es un entero único que identifica cada paquete.
     */
    private Integer id;
    
    /**
     * address identifica la dirección de entrega/recogida de el paquete
     */
    private Address address;
    
    /**
     * Constructora por defecto.
     */
    public Packet(){
    }
    /**
     * Creadora con parametros.
     * @param i el id del paquete.
     * @param a la dirección del paquete.
     * Constructora con parámetros.
     */
    public Packet(Integer i, Address a){
        id = i;
        address = a;

    }

    /**
     * Método que modifica el id del paquete.
     * @param id el nuevo id del paquete.
     */
    public void setId(Integer i){
        id = i;
    }
    
    /**
     * Método que modifica la dirección dle paquete.
     * @param a es la nueva dirección.
     */
    public void setAddress(Address a){
        address = a;
    }
    
    /**
     * Método que devuelve el id del paquete.
     * @return Devuelve el id del paquete.
     */
    public Integer getId(){
        return id;
    }
    
    /**
     * Método que devuelve la dirección del paquete.
     * @return La dirección del paquete.
     */
    public Address getAddress(){
        return address;
    }
    
    /**
     * Método que devuelve el id de la dirección del paquete.
     * @return El id de la dirección del paquete.
     */
    public Integer getAddressId(){
        return address.getId();
    }
    
    /*UNUSED*/
    /**
     * Método que devuelve la dirección del paquete.
     * @return La dirección del paquete.
     */
    public void printPacket(){
        System.out.println("id: "+id+" address: "+address+"\n");

    }
    
    //UNUSED
    public void readPacket(){
        Scanner sc = new Scanner(System.in); 
        
        /*Integer n = sc.nextInt();
        id = n;
        address = sc.nextLine();*/
    }
}