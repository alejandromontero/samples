package domain;
import java.util.*;

/**
 * Clase Elemento.
 * Un elemento tiene un identificador definido por el usuario y un nombre.
 * En esta clase tenemos las creadoras de la clase, y los métodos que definen y obtienen dicho nombre y id.
 *
 * @author xavier.oya
 */
        
public class Element {

    /**
    * Entero que contiene el identificador del elemento.
    */
    protected Integer id;     //id del elemento
    
    /**
    * String que contiene el nombre del elemento.
    */
    protected String name;  //nombre del elemento

    /**
    * Constructora de la clase Elemento.
    * Crea un elemento vacio.
    */
    public Element () {}
    
    /**
    * Constructora de la clase Elemento.
    * Crea un elemento con parámetros predefinidos.
    * @param i El identificador del elemento
    * @param s El nombre del elemento
    */
    public Element (Integer i, String s) {
        id = i;
        name = s;
    }
    
     /**
    * Método que obtiene la id del elemento.
    * La id debe existir previamente.
    * @return un entero con la id del elemento.
    */
    public Integer getId () {
	return id;
    }
    
    /**
    * Método que obtiene el nombre del elemento.
    * El nombre debe existir previamente.
    * @return un string con el nombre del elemento.
    */
    public String getName () {
	return name;
    } 
    
     /**
    * Método que define la id del elemento.
    * El parámetro tiene que ser un valor entero >= 0.
    * @param i id del elemento
    * @return true si se ha realizado, false si no se ha realizado a causa de que algún valor introducido es inválido
    */
    public boolean setId (Integer i) {
       if (i >= 0) {
		id = i;
		return true;
       }
       return false;
    }

    /**
    * Método que define el nombre del elemento.
    * @param n El nombre del elemento
    * @return true si se ha realizado, false si no se ha realizado a causa de que algún valor introducido es inválido
    */
    public boolean setName (String n) {
	name = n;
        return true;
    }

    /**
    * Método que lee un elemento por pantalla.
    * Lee el el nombre y la id por pantalla y las asigna al elemento.
    */
    public void readElement() {
        
        Scanner sc = new Scanner(System.in); 
        Integer i = sc.nextInt();
        if (i >= 0) id = i;
        name = sc.nextLine();
        
    }
        
    /**
    * Método que imprime un elemento por pantalla.
    * Imprime por pantalla el nombre y la id del elemento.
    */
    public void printElement() {
        System.out.print(id);
        System.out.print(" ");
        System.out.println(name);
        System.out.print(" ");
    }
}