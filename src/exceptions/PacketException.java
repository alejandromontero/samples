/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package exceptions;

/**
 *
 * @author gerard
 */
public class PacketException extends Exception{
    public PacketException(String message){
         super(message);
      }
    public PacketException(){
        super();
    }
}
