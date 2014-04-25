package automata;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.Triple;

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
                    if (element.first().name().equals(from.name()) && element.second().equals(c)){
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
		Set<State> estadosDFA = new HashSet<State>();
		Set<Triple<State,Character,State>> transicionesDFA = new HashSet<Triple<State,Character,State>>();
		Set<State> estados_finalesDFA = new HashSet<State>();
		
		//ALGORITMO DE NFALambda a DFA//
		Set<Triple<Set<State>,Character,Set<State>>> T = new HashSet<Triple<Set<State>,Character,Set<State>>>();
		//Calcular Cerradura-lambda (0) = Estado A; 
		Set<State> inicialDFALambda = new HashSet<State>();
		inicialDFALambda.add(inicial);
		Set<State> A = clausura_lambda(inicialDFALambda);
		// Incluír A en NuevosEstados; 
		Set<Set<State>> NuevosEstados = new HashSet<Set<State>>();
		NuevosEstados.add(A);
		// WHILE no están todos los W de NuevosEstados marcados DO BEGIN 
		Set<Set<State>> EstadosMarcados = new HashSet<Set<State>>();
		while(!todosMarcados(NuevosEstados,EstadosMarcados)){
			//Marcar W; 
			Set<State> W = marcarEstado(NuevosEstados, EstadosMarcados);
			//FOR cada ai pertence Te DO BEGIN
			Iterator<Character> iteratorAlfabeto = alfabeto.iterator();
			while (iteratorAlfabeto.hasNext()){
				Character a = iteratorAlfabeto.next();
				//X = Cerradura-lambda (Mueve (W, ai)); 
				Set<State> X = clausura_lambda(mover(W, a));
				// IF X no está en el conjunto NuevosEstados añadirlo;
				if(!perteneceNuevoEstados(NuevosEstados, X)){
					NuevosEstados.add(X);
				}
				//Transición [W, a] = X;
				T.add(new Triple<Set<State>, Character, Set<State>>(W, a, X));
			}
		}

		// FIN //
		
		DFA dfaAutomata = new DFA(estadosDFA, alfabeto, transicionesDFA, inicial, estados_finalesDFA);
		return dfaAutomata;
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
