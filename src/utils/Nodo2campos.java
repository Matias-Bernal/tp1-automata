/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 * Clase que representa un objeto que contiene un objeto de informacion ({@code Object}) y un objecto de la clase {@code Nodo2campos}
 */
public class Nodo2campos {
	/**
	 * El objeto que contiene la informacion
	 */
    private Object info;
    /**
     * Objeto que contiene otro objeto de esta misma clase
     */
    private Nodo2campos next;

    /**
     * @return El contenido del atributo {@code info} : {@code Object}
     */
    public Object getInfo() {
        return info;
    }

    /**
     * Cambia el contenido del atributo {@code info}
     * @param info : nuevo contenido para el atributo {@code info} : {@code Object}
     */
    public void setInfo(Object info) {
        this.info = info;
    }

    /**
     * @return El contenido del atributo {@code next} : {@code Nodo2campos}
     */
    public Nodo2campos getNext() {
        return next;
    }
    
    /**
     * Cambia el contenido del atributo {@code next}
     * @param next : nuevo contenido para el atributo {@code next} : {@code Nodo2campos}
     */
    public void setNext(Nodo2campos next) {
        this.next = next;
    }
    
    
    /*public String toString(){
        return this.info.toString();
    }*/

}