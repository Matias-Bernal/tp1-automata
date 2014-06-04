/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Administrador
 */
public interface TadPilaInterface {
    
    public abstract boolean esVacia();
    
    public abstract Object tope();
    
    public abstract void apilar(Object elemento);
    
    public abstract void desapilar ();
    
    public abstract void vaciar ();
    
    @Override
    public abstract String toString();
}