/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Administrador
 */
public class TadPilaDinPila implements TadPilaInterface{
    private Nodo2campos nodo;
    private Boolean vacia;
    
    
    public TadPilaDinPila() {
        this.nodo = null;
        this.vacia = true;
    }
    
        
    public boolean esVacia() {
        return this.vacia;
    }

    public Object tope() {
        if (!this.esVacia()) {
            return this.nodo.getInfo();
        }
        else {
            return null;
        }
    }

    public void apilar(Object elemento) {
        Nodo2campos nuevoNodo = new Nodo2campos();
        nuevoNodo.setInfo(elemento);
        nuevoNodo.setNext(this.nodo);
        this.nodo = nuevoNodo;
        this.vacia = false;
    }

    public void desapilar() {
        if (!this.esVacia() ) {
            this.nodo = this.nodo.getNext();
        }
        if (this.nodo == null) {
            this.vacia = true;
        }
    }

    public void vaciar() {
        this.nodo = null;
        this.vacia = true;
    }
    
    @Override
    public String toString() {
        String res = "";
        while (!this.esVacia()) {
            res = res + this.tope().toString() + "\n";
            this.desapilar();
        }
        return res;
    }

}