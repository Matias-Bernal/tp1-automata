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
        this.estados = states;
        this.alfabeto = alphabet;
        this.transiciones = transitions;
        this.inicial = initial;
        this.estados_finales = final_states;
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
		// A = clausura_lambda (Q0)
		Set<State> inicialDFALambda = new HashSet<State>();
		inicialDFALambda.add(inicial);
		Set<State> a = clausura_lambda(inicialDFALambda);
		// Incluír A en estados del DFA;
		Iterator<State> iteratorA = a.iterator();
		while (iteratorA.hasNext()){
			State elementA = iteratorA.next();
			if(!estadosDFA.contains(elementA))
				estadosDFA.add(elementA);
		}
		// WHILE no están todos los W de NuevosEstados marcados DO BEGIN
		Set<State> marcados = new HashSet<State>();
		while (marcados.equals(estadosDFA)){
			
		}
		
		DFA dfaAutomata = new DFA(estadosDFA, alfabeto, transicionesDFA, inicial, estados_finalesDFA);
		return dfaAutomata;
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
