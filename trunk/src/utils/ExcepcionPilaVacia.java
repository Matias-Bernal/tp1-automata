/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 * Clase usada para tratar el caso de obtener tope de una pila vacia

 */
@SuppressWarnings("serial")
public class ExcepcionPilaVacia extends RuntimeException {


	/**
	 * @param arg0 = mensaje de la excepcion : {@code String}
	 */
	public ExcepcionPilaVacia(String arg0) {
		super(arg0);
	}


}
