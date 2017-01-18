package persistence;

import exceptions.FileException;
import exceptions.MapException;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Clase FileManagerController.
 * La Clase FileManagerController se encarga de cargar/guardar los ficheros de 
 * texto de tipo AddressSet, RelationSetExt, PacketSet, SolutionExtended.
 *
 * @author Gerard Alonso
 */
public class FileManagerController {
    
    private final String RegExpAddress="Avinguda\\d+ Carrer\\d+";
    private final String RegExpRelation = RegExpAddress + ";{1}" + RegExpAddress + ";{1}\\d{1,3}";
    /**
    * Método que carga las direcciones desde un fichero de texto.
    * @param filename Nombre del fichero a cargar. En cada linea una dirección.
    * @return Un vector de Strings con todas las direcciones obtenidas del fichero.
    * @throws FileException Cuando hay algun problema con el fichero de entrada.
    * @throws MapException Cuando el formato no es el correcto.
    */
    public Vector<String> loadAddress(String filename) throws FileException, MapException {

        /*Declaraciones*/
        File f;
        FileReader fr = null;
        BufferedReader br;
        String str;
        Vector<String> v;
        boolean encontrado=false;
        try {
            /*Inicializaciones*/
            v = new Vector<>();
            
            /*Apertura del fichero*/
            f = new File(filename);
            fr = new FileReader(f);
            br = new BufferedReader(fr);

            /*Lectura del fichero*/
            str = br.readLine();
            while(str != null && !encontrado) {
                if(str.startsWith("#")) encontrado=true;
                
                if(!encontrado){
                    if(!str.matches(RegExpAddress)) throw new FileException("The file content is not correct(address)");
                    v.addElement(str);
                }  
                    str = br.readLine();
                } 
               /*Retorno del contenido leido*/
                if(v.size()<3) throw new MapException("Not enough address");
                return v;   
        } catch (FileNotFoundException ex) {
            throw new FileException("File not found");
        } catch (IOException ex) {
            throw new FileException("Input error");
        } finally {
            try {
                /*Cerramos el fichero*/
                if(fr != null) fr.close();
            } catch (IOException ex) {
                throw new FileException("File closing error");
            }
        }
    }
    
    /**
    * Método que carga las relaciones desde un fichero de texto.
    * @param filename Nombre del fichero a cargar. En cada linea una 
    * relación(address1;address2;value).
    * @return Un vector de ArrayList de Strings, donde cada ArrayList contiene 
    * una relación.
    * @throws FileException Cuando el formato del fichero no es correcto o si hay
    * algun problema con el fichero.
    */
    public Vector<ArrayList<String>> loadRelations(String filename) throws FileException {

        /*Declaraciones*/
        File f;
        FileReader fr = null;
        BufferedReader br;
        ArrayList<String> e;
        String str;
        Vector<ArrayList<String>> v;
        boolean encontrado=false;
        try {
            /*Inicializaciones*/
            v = new Vector<>();
            
            /*Apertura del fichero*/
            f = new File(filename);
            fr = new FileReader(f);
            br = new BufferedReader(fr);

            /*Lectura del fichero*/
            str = br.readLine();
            while(str != null) {
                if(str.startsWith("#")) {
                        encontrado=true;
                        str = br.readLine();
                    }
                if(encontrado){
                   
                    if(str.length()>0 && !str.matches(RegExpRelation)) {
                        throw new FileException("The file content is not correct(relation)");
                    }
                    e = new ArrayList<>();
                    String a[] = str.split(";");
                    e.add(a[0]);
                    e.add(a[1]);
                    e.add(a[2]);
                    v.addElement(e);
                }  
                    str = br.readLine();
                } 
               /*Retorno del contenido leido*/
                return v;   
       } catch (FileNotFoundException ex) {
            throw new FileException("File not found");
        } catch (IOException ex) {
            throw new FileException("Input error");
        } finally {
            try {
                /*Cerramos el fichero*/
                if(fr != null) fr.close();
            } catch (IOException ex) {
                throw new FileException("File closing error");
            }
        }
    }
    
    /**
    * Método que carga los paquetes o una solución desde un fichero de texto.
    * @param filename Nombre del fichero a cargar. En cada linea una dirección 
    * de un paquete o punto de solución.
    * @return Un vector de Strings con todas las direcciones de los paquetes 
    * obtenidas del fichero, o con la solución.
    * @throws FileException Cuando hay algun problema con el fichero de entrada.
    */
    public Vector<String> loadPacketOrSolution(String filename) throws FileException{
        File f;
        FileReader fr = null;
        BufferedReader br;
        ArrayList<String> e;
        String str;
        Vector<String> v;
        try {
            v = new Vector<>();
            e = new ArrayList<>();
            
            /*Apertura del fichero*/
            f = new File(filename);
            fr = new FileReader(f);
            br = new BufferedReader(fr);

            /*Lectura del fichero*/
            str = br.readLine();
            while(str != null) {
                    v.addElement(str); 
                    str = br.readLine();
             } 
             return v;   
        } catch (FileNotFoundException ex) {
            throw new FileException("File not found");
        } catch (IOException ex) {
            throw new FileException("Input error");
        } finally {
            try {
                /*Cerramos el fichero*/
                if(fr != null) fr.close();
            } catch (IOException ex) {
                throw new FileException("File closing error");
            }
        }
    }
    

    /**
    * Método que guarda una Solution en un fichero de texto. Reescribe la información
    * que havia en el fichero. Formato de salida, direcciones, una en cada linea.
    * @param fTarget Nombre del fichero destino.
    * @param vs Un vector de Strings que contiene el conjunto de direcciones.
    * @throws FileException Cuando hay algun problema con el fichero de salida.
    */
    public void saveSolution(String fTarget, Vector<String> vs) throws FileException {
        File f;
        FileWriter fw = null;
        PrintWriter pw;
        try {
            f = new File(fTarget);
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);
            
            /*Escritura del fichero*/
            for (int i = 0; i < vs.size(); ++i) {
                pw.println(vs.get(i));
            }
        } catch (IOException ex) {
            throw new FileException("Input error");
        } finally {
            try {
                if(fw != null) fw.close();
            } catch (IOException ex) {
                throw new FileException("File closing error");
            }
        }
    }
    
    /**
    * Método que guarda el estado actual del mapa en un fichero de texto. Formato
    * del fichero: direcciones (una en cada linia), # al acabar. Despues las 
    * relaciones: dirección;dirección;distancia(una en cada linea).
    * @param fTarget Nombre del fichero destino.
    * @param va Un vector  de Strings que contiene todas las direcciones.
    * @param vr Un vector de ArrayList de Strings donde estan guardadas todas
    * las relaciones.
    * @throws FileException Cuando hay algun problema con el fichero de salida.
    */
    public void saveMaps(String fTarget, Vector<String> va, Vector<ArrayList<String>> vr) throws FileException {
        File f;
        FileWriter fw = null;
        PrintWriter pw;
        try {            
            f = new File(fTarget);
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);
            
            /*Escritura del fichero*/
            for (int i = 0; i < va.size(); ++i) {
                pw.println(va.get(i));
            }
            pw.println("#");
            for (int i = 0; i < vr.size(); ++i) {
                pw.println(vr.get(i).get(0)+";"+vr.get(i).get(1)+";"+vr.get(i).get(2));
            }
        } catch (IOException ex) {
            throw new FileException("Input error");
        } finally {
            try {
                if(fw != null) fw.close();
            } catch (IOException ex) {
               throw new FileException("File closing error");
            }
        }
    }
    
    /**
    * Método que guarda el estado actual de los paquetes a un fichero de texto.
    * Formato: una dirección de paquete en cada linea.
    * @param fTarget Nombre del fichero destino.
    * @param vp Un vector de Strings que contiene las direcciones de los paquetes.
    * @throws FileException Cuando hay algun problema con el fichero de salida.
    */
    public void savePackets(String fTarget, Vector<String> vp) throws FileException {
        File f;
        FileWriter fw = null;
        PrintWriter pw;
        try {
            /*Apertura del fichero y prepararlo para escribir*/
            f = new File(fTarget);
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);
            
            /*Escritura del fichero*/
            for (int i = 0; i < vp.size(); ++i) {
                pw.println(vp.get(i));
            }
        } catch (IOException ex) {
             throw new FileException("Input error");
        } finally {
            try {
                if(fw != null) fw.close();
            } catch (IOException ex) {
                throw new FileException("File closing error");
            }
        }
    }
}