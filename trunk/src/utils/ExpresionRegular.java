package utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import automata.DFA;
import automata.FA;
import automata.NFALambda;
import automata.State;

public class ExpresionRegular {
	
	private String expresion;

	public ExpresionRegular(String expresion) {
		assert expresion!=null;
		assert !expresion.isEmpty();
		
		this.expresion = expresion;
	}

	public String getExpresion() {
		return expresion;
	}

	public void setExpresion(String expresion) {
		this.expresion = expresion;
	}
	
	public DFA S(){
        DFA res = E(obtenerToken());
        if(obtenerToken().equals("~")){
        	return res;
        }else{
        	return null;
        }
	}
	
    private DFA E(String token) {
    	DFA res = T(token);
        if(res!=null){
        	String auxtoken = obtenerToken();
			if(auxtoken.equals("|") && !token.equals("~")){
				ConsumirToken("|");
				return  res.union(T(obtenerToken()));
			}else{
				return res;
			}
        }else{
        	return null;
        }
    } 
    
    
	private DFA T(String token) {
    	DFA res = F(token);
        if(res!=null){
			String auxtoken = obtenerToken();
			if(auxtoken.equals(".") && !token.equals("~")){
				ConsumirToken(".");
				return  res.intersection(F(obtenerToken()));
			}else{
				return res; //aca devuelve true
			}
		}else{
			return null;
		}
	}
	
	
	private DFA F(String token) {
		DFA res = L(token);
        if(res!=null){
			String auxtoken = obtenerToken();
			if(auxtoken.equals("*") && !token.equals("~")){
				ConsumirToken("*");
				res = res.star();
				Set<Triple<State,Character,State>> transitions = res.getTransiciones();
				State initial = res.initial_state();
							
				Iterator<State> iterator = res.final_states().iterator();
	            while (iterator.hasNext()){
	            		State final_state = iterator.next();
	                    Triple<State,Character,State> element = new Triple<State, Character, State>(initial,FA.Lambda,final_state); 
	                    if (!transitions.contains(element))
	                    	transitions.add(element);
	            }
	            
	            NFALambda nfaAutomata = new NFALambda(res.states(), res.alphabet(), transitions, initial, res.final_states());
				return nfaAutomata.toDFA(); //aca devuelve true
			}else{
				return res;
			}
		}else{
			return null;
		}
	}
	
	private DFA L(String token) {
		//consumirVacios();
        //String token = obtenerToken();
		if(token.equals("(")){
			ConsumirToken("(");
			DFA res = E(obtenerToken());
			if(res!=null && ConsumirToken(")")){
				return res;				
			}else{
				return null;
			}
		}else{
			DFA res = AZ(token);
			if(res!=null){
				return res;				
			}else{
				return null;
			}
		}
	}

	private DFA AZ(String token) {
		//consumirVacios();
		Pattern pat = Pattern.compile("[a-z]");
		Matcher mat = pat.matcher(token);
		if(mat.matches()){
			ConsumirToken(token);
			State iniState = new State("p0");
			Set<State> states = new HashSet<State>();
			states.add(iniState);
			State final_state = new State("p1");
			states.add(final_state);
			Set<State> final_states = new HashSet<State>();
			final_states.add(final_state);
			Triple<State, Character, State> transition = new Triple<State, Character, State>(iniState, token.charAt(0), final_state);
			Set<Triple<State, Character, State>> transitions = new HashSet<Triple<State, Character, State>>();
			transitions.add(transition);
			Set<Character> alphabet = new HashSet<Character>();
			alphabet.add(token.charAt(0));
									
			return new DFA(states, alphabet, transitions, iniState, final_states);
		}else{
			return null;
		}
	}
	
	
	public DFA toDFA(){
        this.expresion = expresion.concat("~"); // agregamos ~ como marca final
        DFA automata = S();
        return automata.minimizeDFA(automata);
	}
	
	/*
	* Metodo utilizado para consumir un token de la cadena.
	*/
    private boolean ConsumirToken (String token){
       if (expresion.length()>0){
          if(expresion.length()>0 && (expresion.charAt(0))==(token.charAt(0))){
        	  expresion = expresion.substring(1);
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
        return String.valueOf(expresion.charAt(0));
    }

}
