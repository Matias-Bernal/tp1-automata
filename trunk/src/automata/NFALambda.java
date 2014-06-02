package automata;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.Triple;
import utils.Tuple;

public class NFALambda extends FA {
	
	/*
	 *  Construction
	*/
	
	// Constructor
	public NFALambda(
			Set<State> states,
			Set<Character> alphabet,
			Set<Triple<State,Character,State>> transitions,
			State initial, 
			Set<State> final_states)
	throws IllegalArgumentException
	{
		assert states != null;
		assert alphabet != null;
		assert transitions != null;
		assert final_states != null;
		
        this.estados = states;
        this.alfabeto = alphabet;
        if(!alfabeto.contains(FA.Lambda))
        	alfabeto.add(FA.Lambda);
        this.transiciones = transitions;
        this.inicial = initial;
        this.estados_finales = final_states;
        if(!rep_ok()){
        	throw new IllegalArgumentException();
        }
	}

	
	/*
	 *	State querying 
	*/

	@Override
	public Set<State> states() {
		// TODO
		return this.estados;
	}

	@Override
	public Set<Character> alphabet() {
		// TODO
		return this.alfabeto;
	}
	
	@Override
	public State initial_state() {
		// TODO
		return this.inicial;
	}

	@Override
	public Set<State> final_states() {
		// TODO
		return this.estados_finales;
	}

	@Override
	public Set<State> delta(State from, Character c) {
		assert states().contains(from);
		assert alphabet().contains(c);
		// TODO
        assert !(transiciones.isEmpty());
        Set<State> result = new HashSet<State>();
        Iterator<Triple<State,Character,State>> iterator = transiciones.iterator();
        while (iterator.hasNext()){
            Triple<State,Character,State> element = iterator.next();
            if (element.first().equals(from) && element.second().equals(c) || element.first().equals(from) && element.second().equals(FA.Lambda)){
                result.add(element.third());
            }
        }
		return result;
		
	}

	@Override
	public String to_dot() {
		assert rep_ok();
		// TODO
		String dot_graph = "digraph{\n"+
				"inic[shape=point];\n"+
				"inic->"+initial_state().name()+";\n";
		assert !(transiciones.isEmpty());
		Iterator<Triple<State,Character,State>> iterator = transiciones.iterator();
		while (iterator.hasNext()){
		Triple<State,Character,State> element = iterator.next();
		dot_graph += element.first().name()+"->"+element.third().name()+" [label=\""+element.second().toString()+"\"];\n";
		}
		dot_graph += "\n";
		assert !(transiciones.isEmpty());
		Iterator<State> iteratorfinal = final_states().iterator();
		while (iteratorfinal.hasNext()){
		State element = iteratorfinal.next();
		dot_graph += element.name()+"[shape=doublecircle];\n"; 
		}
		dot_graph += "}";
		return dot_graph;
	}

	
	/*
	 *  Automata methods
	*/	
	
	
	@Override
	public boolean accepts(String string) {
		assert rep_ok();
		assert string != null;
		assert verify_string(string);
		// TODO
		if(string==""){
			Set<State> finales = delta(inicial, FA.Lambda);
			return finales.contains(inicial);
		}else{
            Set<State> sucesor1 = new HashSet<State>();
            sucesor1.addAll(delta(inicial,string.charAt(0)));
            
            if(sucesor1.isEmpty()){return false;}
            
            Set<State> sucesor2 = new HashSet<State>();
            int i = 1;
            while(i < string.length()){
                if ((i%2) != 0){
                    sucesor2.addAll(deltaSet(sucesor1, string.charAt(i)));
                    sucesor1.clear();
                    if(sucesor2.isEmpty()){
                        return false;
                    }
                }else{
                    sucesor1.addAll(deltaSet(sucesor2, string.charAt(i))); 
                    sucesor2.clear();
                    if(sucesor1.isEmpty()){
                        return false;
                    }
                }
                i++;
            }
            if (sucesor1.isEmpty()){
                return containFinal(sucesor2, estados_finales);
            }else{
                return containFinal(sucesor1, estados_finales);
            }
		}
	}
	
   public Set<State> deltaSet(Set<State> from, Character c) {
        assert states().containsAll(from);
        assert alphabet().contains(c);
        // TODO
        assert !(transiciones.isEmpty());
        Set<State> result = new HashSet<State>();
        Iterator<Triple<State,Character,State>> iterator = transiciones.iterator();
        while (iterator.hasNext()){
            Triple<State,Character,State> element = iterator.next();
            if (from.contains(element.first()) && element.second().equals(c)){
                result.add(element.third());
            }
        }
        return result;	
	}
        
    //funcion que cheque si un set contiene por lo menos un estado final
    private boolean containFinal(Set<State> states, Set<State> finals){
        Iterator<State> iterator = states.iterator();
        while(iterator.hasNext()){
            State element = iterator.next();
            if(finals.contains(element)){
                return true;
            }
        }
        return false;
    }
	
	
	public Set<State> clausura_lambda(Set<State> estado){
		assert !(transiciones.isEmpty());
		Set<State> result = estado;
		Iterator<State> iteratorState = estado.iterator();
		Iterator<Triple<State,Character,State>> iteratorTrasitions = transiciones.iterator();
		while (iteratorState.hasNext()){
			State element = iteratorState.next();
			while (iteratorTrasitions.hasNext()){
				Triple<State,Character,State> transition = iteratorTrasitions.next();
				if(transition.first().equals(element) && transition.second().equals(Lambda)){
					if (!result.contains(transition.third())){
						result.add(transition.third());
						Set<State> aux = new HashSet<State>();
						aux.add(transition.third());
						Set<State> aux2 = clausura_lambda(aux);
						Iterator<State> iteratorAux = aux2.iterator();
						while (iteratorAux.hasNext()){
							State elementAux = iteratorAux.next();
							if (!result.contains(elementAux))
								result.add(elementAux);
						}
					}
				}
			}
		}
		return result;
	}
	
	public Set<State> mover (Set<State> estado, Character a){
		Set<State> result = new HashSet<State>();
		Iterator<State> iteratorState = estado.iterator();
		while (iteratorState.hasNext()){
			State element = iteratorState.next();
			Set<State> aux = delta(element,a);
			Iterator<State> iteratorAux = aux.iterator();
			while (iteratorAux.hasNext()){
				State elementaux = iteratorAux.next();
				if(!result.contains(elementaux))
					result.add(elementaux);
			}
		}
		return result;
	}
	
	/**
	 * Converts the automaton to a DFA.
	 * 
	 * @return DFA recognizing the same language.
	*/
	public DFA toDFA() {
		assert rep_ok();
		// TODO		
		//ALGORITMO DE NFALambda a DFA//
		Set<Triple<Set<State>,Character,Set<State>>> T = new HashSet<Triple<Set<State>,Character,Set<State>>>();
		//Calcular Cerradura-lambda (0) = Estado A; 
		Set<State> inicialDFALambda = new HashSet<State>();
		inicialDFALambda.add(inicial);
		Set<State> A = clausura_lambda(inicialDFALambda);
		// Inclu�r A en NuevosEstados; 
		Set<Set<State>> NuevosEstados = new HashSet<Set<State>>();
		NuevosEstados.add(A);
		Set<Set<State>> NuevosEstadosFinales = new HashSet<Set<State>>();
		Iterator<State> iteratorFianales = estados_finales.iterator();
		while (iteratorFianales.hasNext()){
			State estadofinal = iteratorFianales.next();
			if(A.contains(estadofinal)){
				NuevosEstadosFinales.add(A);
				break;
			}
		}
		// WHILE no est�n todos los W de NuevosEstados marcados DO BEGIN 
		Set<Set<State>> EstadosMarcados = new HashSet<Set<State>>();
		while(!todosMarcados(NuevosEstados,EstadosMarcados)){
			//Marcar W; 
			Set<State> W = new HashSet<State>(); 
			Iterator<Set<State>> iteratorNuevosEstados = NuevosEstados.iterator();
			while (iteratorNuevosEstados.hasNext()){
				Set<State> elemenNuevoEstado = iteratorNuevosEstados.next();
				if(!EstadosMarcados.contains(elemenNuevoEstado)){
					W = elemenNuevoEstado;
					EstadosMarcados.add(W);
					break;
				}
			}
			//FOR cada ai pertence Te DO BEGIN
			Iterator<Character> iteratorAlfabeto = alfabeto.iterator();
			while (iteratorAlfabeto.hasNext()){
				Character a = iteratorAlfabeto.next();
				if(!a.equals(FA.Lambda)){
					//X = Cerradura-lambda (Mueve (W, ai));
					Set<State> X = new HashSet<State>();
					X = clausura_lambda(mover(W, a));
					// IF X no est� en el conjunto NuevosEstados a�adirlo;
					if(!perteneceNuevoEstados(NuevosEstados, X) && !X.isEmpty()){
						NuevosEstados.add(X);
					}
					//Transici�n [W, a] = X;
					if(!X.isEmpty()){
						T.add(new Triple<Set<State>, Character, Set<State>>(W, a, X));
						Iterator<State> iteratorFianalesX = estados_finales.iterator();
						while (iteratorFianalesX.hasNext()){
							State estadofinal = iteratorFianalesX.next();
							if(X.contains(estadofinal) && !NuevosEstadosFinales.contains(X)){
								NuevosEstadosFinales.add(X);
								break;
							}
						}
					}
				}
			}
		}

		// falta renombrar cada tuple<estado, estado> con un estado  
	    Set<State> new_estados = new HashSet<State>();
	    Set<State> new_estados_finales = new HashSet<State>();
	    int i = 0;
	    State newinitial = new State("p"+i);
	    new_estados.add(newinitial);
	    i++;
	    Set<Tuple<Set<State>,State>> estados_Guardados = new HashSet<Tuple<Set<State>,State>>(); 
	    estados_Guardados.add(new Tuple<Set<State>, State>(inicialDFALambda, newinitial));
   
	    if(NuevosEstadosFinales.contains(inicialDFALambda))
            new_estados_finales.add(newinitial);
    
	    Set<Triple<State,Character,State>> transiciones_Guardadas = new HashSet<Triple<State,Character,State>>();
	    Iterator<Triple<Set<State>, Character, Set<State>>> iteratorT = T.iterator();
    
	    while(iteratorT.hasNext()){
	    	Triple<Set<State>, Character, Set<State>> transition_element = iteratorT.next();
	    	State newFromState = devolverGuardado(estados_Guardados, transition_element.first());  
	    	if(newFromState==null){
              newFromState = new State("p"+i);
              new_estados.add(newFromState);
              i++;
              estados_Guardados.add(new Tuple<Set<State>, State>(transition_element.first(),newFromState));
	    	}
	    	if(NuevosEstadosFinales.contains(transition_element.first()) && !new_estados_finales.contains(newFromState))
              new_estados_finales.add(newFromState);
      
	    	State newToState = devolverGuardado(estados_Guardados, transition_element.third());
	    	if(newToState==null){
	    		newToState = new State("p"+i);
	    		new_estados.add(newToState);
	    		i++;
	    		estados_Guardados.add(new Tuple<Set<State>, State>(transition_element.third(),newToState));
	    	}
      
	    	if(NuevosEstadosFinales.contains(transition_element.third()) && !new_estados_finales.contains(newToState))
	    		new_estados_finales.add(newToState);
      
	    	Triple<State,Character,State> newTransition = new Triple<State, Character, State>(newFromState, transition_element.second(),  newToState);
	    	if(!transiciones_Guardadas.contains(newTransition)){
	    		transiciones_Guardadas.add(newTransition);
	    	}
      
	    }
    
	    Set<Character> nuevo_alfabeto = new HashSet<Character>();
	    nuevo_alfabeto = alfabeto;
	    nuevo_alfabeto.remove(FA.Lambda);
	    DFA dfa = new DFA(new_estados, nuevo_alfabeto,transiciones_Guardadas,newinitial,new_estados_finales);	
		// FIN //
			
		return dfa;
	}
	
    public State devolverGuardado(Set<Tuple<Set<State>,State>> conjunto,Set<State> estado){
        State res = null;
        Iterator<Tuple<Set<State>, State>> iterator = conjunto.iterator();
		while(iterator.hasNext()){
		  Tuple<Set<State>, State> element = iterator.next();
		  if(element.first().equals(estado))
		          return element.second();
		}
	    return res;
	}
	
	public boolean perteneceNuevoEstados(Set<Set<State>> NuevosEstados,Set<State> estado){
		return (NuevosEstados.contains(estado));
	}
		
	public boolean todosMarcados(Set<Set<State>> NuevosEstados,Set<Set<State>> EstadosMarcados){
		Iterator<Set<State>> iteratorNuevosEstados = NuevosEstados.iterator();
		Boolean res = true;
		while (iteratorNuevosEstados.hasNext()){
			Set<State> elemenNuevoEstado = iteratorNuevosEstados.next();
			if(!EstadosMarcados.contains(elemenNuevoEstado)){
				res = false;
				break;
			}
		}
		return res;
	}
	
	public Set<State> marcarEstado(Set<Set<State>> NuevosEstados,Set<Set<State>> EstadosMarcados){
		Set<State> marcado = null;
		Iterator<Set<State>> iteratorNuevosEstados = NuevosEstados.iterator();
		while (iteratorNuevosEstados.hasNext()){
			Set<State> elemenNuevoEstado = iteratorNuevosEstados.next();
			if(!EstadosMarcados.contains(elemenNuevoEstado)){
				marcado = elemenNuevoEstado;
				break;
			}
		}
		return marcado;
	}
	
	@Override
	public boolean rep_ok() {
		// TODO: Check that initial and final states are included in 'states'.
		// TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
                if ((estados.contains(inicial)) && (checkFinalStates()) && (checkTransition())){
                    return true;
                }
                return false;
	} 

        private boolean checkFinalStates() {
            Iterator<State> iterator = estados_finales.iterator();
            while (iterator.hasNext()){
                if (!estados.contains(iterator.next())){
                    return false;
                }
            }
            return true;
        }

        private boolean checkTransition() {
            Iterator<Triple<State,Character,State>> iterator = transiciones.iterator();
            while (iterator.hasNext()){
                Triple<State,Character,State> element = iterator.next();        
                if (!estados.contains(element.first()) ||  !alfabeto.contains(element.second()) || !estados.contains(element.third())){
                    return false;
                }
            }
            return true;
        }
}