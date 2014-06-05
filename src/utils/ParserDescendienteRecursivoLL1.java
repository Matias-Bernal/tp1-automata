package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserDescendienteRecursivoLL1 {

    private String cadena;/*cadena a parsear*/

    public ParserDescendienteRecursivoLL1(String cadena) {
    	assert cadena!=null;
        this.cadena = cadena.concat("~"); // agregamos ~ como marca final
    }

	/*
	 * Metodo metodo que inicializa el parser  llamando al metodo
	 * que representa nuestro estado inicial.
	*/
    public boolean iniciarParser (){
    	if (!cadena.isEmpty()){
    		return S();
    	}else{
    		return false;
    	}
    }

	/*
	 * Metodo que nos representa la definicion de las producciones de S.
	 * retorna true en caso de ejecutarse correctamente,en caso de error
	 * retorna false.
	 */
    private boolean S(){
        String token = obtenerToken();
        if(token.equals("~")){
        	return true;
        }else{
        	return E(token) && (obtenerToken().equals("~"));
        }
    }

	/*
	 * Metodo que nos representa la definicion de las producciones de E.
	 * retorna true en caso de ejecutarse correctamente,en caso de error
	 * retorna false.
	 */
    private boolean E(String token) {
        if(T(token)){
        	String auxtoken = obtenerToken();
			if(auxtoken.equals("|") && !token.equals("~")){
				return ConsumirToken("|") && T(obtenerToken());
			}else{
				return true;
			}
        }else{
        	return false;
        }
    }  
    

	/*
	 * Metodo que nos representa la definicion de las producciones de T.
	 * retorna true en caso de ejecutarse correctamente,en caso de error
	 * retorna false.
	 */	
	private boolean T(String token) {
		if(F(token)){
			String auxtoken = obtenerToken();
			if(auxtoken.equals(".") && !token.equals("~")){
				return ConsumirToken(".") && F(obtenerToken());
			}else{
				return true; //aca devuelve true
			}
		}else{
			return false;
		}
	}
	
	/*
	 * Metodo que nos representa la definicion de las producciones de F.
	 * retorna true en caso de ejecutarse correctamente,en caso de error
	 * retorna false.
	 */
	private boolean F(String token) {
		//consumirVacios();
        //String token = obtenerToken();
		if(L(token)){
			String auxtoken = obtenerToken();
			if(auxtoken.equals("*") && !token.equals("~")){
				return ConsumirToken("*"); //aca devuelve true
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	
	/*
	 * Metodo que nos representa la definicion de las producciones de L.
	 * retorna true en caso de ejecutarse correctamente,en caso de error
	 * retorna false.
	 */
	private boolean L(String token) {
		//consumirVacios();
        //String token = obtenerToken();
		if(token.equals("(")){
			return ConsumirToken("(") && E(obtenerToken()) && ConsumirToken(")");
		}else{
			return AZ(token);
		}
	}

	/*
	 * Metodo que nos representa la definicion de las producciones de AZ.
	 * retorna true en caso de ejecutarse correctamente,en caso de error
	 * retorna false.
	 */
	private boolean AZ(String token) {
		//consumirVacios();
		Pattern pat = Pattern.compile("[a-z]");
		Matcher mat = pat.matcher(token);
		if(mat.matches()){
			ConsumirToken(token);
			return true;
		}else{
			return false;
		}
	}

	/*
	* Metodo utilizado para consumir un token de la cadena.
	*/
    private boolean ConsumirToken (String token){
       if (cadena.length()>0){
          if(cadena.length()>0 && (cadena.charAt(0))==(token.charAt(0))){
        	  cadena = cadena.substring(1);
        	  return true;
          }else {
        	  return false;
          }
       }else{
    	   return false;
       }
    }

	/*
	 * Metodo que nos retorna el primer token de la cadena.
	*/
    private String obtenerToken() {
        return String.valueOf(cadena.charAt(0));
    }


}

