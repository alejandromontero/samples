/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package exceptions;

/**
 *
 * @author Pau
 */
public class DrawException extends Exception{
    
    
    public DrawException(String cadena) {
        super(cadena);
    }
}
