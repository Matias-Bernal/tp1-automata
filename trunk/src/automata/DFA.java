package automata;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.Triple;
import utils.Tuple;

/* Implements a DFA (Deterministic Finite Atomaton).
*/
public class DFA extends FA {

	/*	
	 * 	Construction
	*/
	
	// Constructor
	public DFA(	Set<State> states,
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
                //TODO
		return this.estados;
	}

	@Override
	public Set<Character> alphabet() {
                //TODO
		return this.alfabeto;
	}
	
	@Override
	public State initial_state() {
                //TODO
		return this.inicial;
	}

	@Override
	public Set<State> final_states() {
		// TODO
		return this.estados_finales;
	}
	
	@Override
	public State delta(State from, Character c) {
		assert states().contains(from);
		assert alphabet().contains(c);
                // TODO
                assert !(transiciones.isEmpty());
                Iterator<Triple<State,Character,State>> iterator = transiciones.iterator();
                while (iterator.hasNext()){
                    Triple<State,Character,State> element = iterator.next();
                    if (element.first().name().equals(from.name()) && element.second().equals(c)){
                        return element.third();
                    }
                }
		return null;
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
				return estados_finales.contains(inicial);
			}else{
                State state = delta(inicial, string.charAt(0));
                if (state == null) {return false;}
                for(int i=1; i<string.length(); i++){
                    state = delta(state, string.charAt(i));
                    if (state == null) {return false;}
                }
                return estados_finales.contains(state);
			}
	}

	/**
	 * Converts the automaton to a NFA.
	 * 
	 * @return NFA recognizing the same language.
	 */
	public NFA toNFA() {
		assert rep_ok();
		// TODO
		NFA nfaAutomata = new NFA(estados, alfabeto, transiciones, inicial, estados_finales);
		return nfaAutomata;
	}
	
	/**
	 * Converts the automaton to a NFALambda.
	 * 
	 * @return NFALambda recognizing the same language.
	 */
	public NFALambda toNFALambda() {
		assert rep_ok();
		// TODO
                Iterator<State> iterator = estados.iterator();
                Set<Triple<State, Character, State>> transicionesNFALambdaSet = new HashSet<Triple<State, Character, State>>(transiciones);
                Set<Character> alfabetoNFALambda = new HashSet<Character>(alfabeto);
                while (iterator.hasNext()){
                    State element = iterator.next();
                    Triple<State, Character, State> transicionLambda = new Triple<State, Character, State>(element, Lambda, element);
                    transicionesNFALambdaSet.add(transicionLambda);
                }
                alfabetoNFALambda.add(Lambda);
		NFALambda nfaLamdaAutomata = new NFALambda(estados, alfabetoNFALambda, transicionesNFALambdaSet, inicial, estados_finales);
		return nfaLamdaAutomata;
	}

	/**
	 * Checks the automaton for language emptiness.
	 * 
	 * @returns True iff the automaton's language is empty.
	 */
	public boolean is_empty() {
		assert rep_ok();
		// TODO       
		if(estados_finales.contains(inicial)){
                return false;
            }
            if(estados.isEmpty() || inicial==null || transiciones.isEmpty() || estados_finales.isEmpty()){
                return true;
            }
            
            //me fijo si existe una cadena que partiendo desde el estado inicial llego a algun estado final
            Set<State> sucesor1 = new HashSet<State>();
            Set<State> sucesor2 = new HashSet<State>();
            sucesor1.add(inicial);
            boolean res = false;
            int i = 0; 
            while (res == false){
                if ((i%2) == 0){
                    sucesor2.addAll(sucesores(sucesor1, transiciones));
                    sucesor1.clear();
                    if(sucesor2.isEmpty()){
                        return false;
                    }else{
                        res= containFinal(sucesor2, estados_finales);
                    }
                }else{
                    sucesor1.addAll(sucesores(sucesor2, transiciones)); 
                    sucesor2.clear();
                    if(sucesor1.isEmpty()){
                        return false;
                    }else{
                        res= containFinal(sucesor1, estados_finales);
                    }
                }
                i++;
            }
            return !res;
	}
        
       // funcion que a base de un set de estados y un arreglo de transiciones, devuelve un set de sucesores de todos los estados ingresados como parametro
        private Set<State> sucesores(Set<State> estados, Set<Triple<State, Character, State>> transiciones){
            Set<State> result = new HashSet<State>();
            Iterator<Triple<State, Character, State>> iterator = transiciones.iterator();
            while (iterator.hasNext()){
                    Triple<State, Character, State> element = iterator.next();
                    if(estados.contains(element.first())){
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
        
	/**
	 * Checks the automaton for language infinity.
	 * 
	 * @returns True iff the automaton's language is finite.
	 */
	public boolean is_finite() {
            assert rep_ok();
            Set<State> visitados = new HashSet<State>();
            Iterator<Triple<State,Character,State>> iterator = transiciones.iterator();
            while(iterator.hasNext()){
                Triple<State,Character,State> element = iterator.next();
                if(!visitados.contains(element.third())){
                    visitados.add(element.first());
                }else{
                    return false;
                }
            }
            return true; 
	}
	
	/**
	 * Returns a new automaton which recognizes the complementary
	 * language. 
	 * 
	 * @returns a new DFA accepting the language's complement.
	 */
	public DFA complement() {
		assert rep_ok();
		// TODO
                Set<Triple<State,Character,State>> new_transiciones = new HashSet<Triple<State,Character,State>>();
                Set<State> new_estados = new HashSet<State>();
                Set<State> new_est_final = new HashSet<State>();
                // cambio los estados no terminales en terminas y viceversa
                Iterator<State> iterator = estados.iterator();
                while(iterator.hasNext()){
                    State element = iterator.next();
                    if (!estados_finales.contains(element)){
                        new_est_final.add(element);
                    }
                }
                // estado trampa
                State estado_Trampa = new State("t0");
                new_estados.addAll(estados);
                
                //agrego todas las transiciones al estado trampa que hacen falta para que sea completo
                new_transiciones.addAll(transiciones);

                //agrego las transiciones que no estan al estado trampa
                
                Iterator<State> iteratorEstados = estados.iterator();
                boolean haytrampa = false;
                
                while(iteratorEstados.hasNext()){
                    State elementEstado = iteratorEstados.next();
                    Iterator<Character> iterator_alfabeto = alfabeto.iterator();
                    while(iterator_alfabeto.hasNext()){
                    	Character element_alfabeto = iterator_alfabeto.next();
                    	Iterator<Triple<State,Character,State>> iterator_transiciones = transiciones.iterator();
                        boolean agregar = true;
                    	while(iterator_transiciones.hasNext()){             	
                            Triple<State,Character,State> element_transiciones = iterator_transiciones.next();
                            if(element_transiciones.first().equals(elementEstado) && element_transiciones.second().equals(element_alfabeto)){
                            	agregar = false;
                            	break;
                            }
                    	}
                    	if(agregar){
                    		haytrampa = true;
                    		Triple<State,Character,State> new_trans_trampa = new Triple<State,Character,State>(elementEstado,element_alfabeto,estado_Trampa);
                            new_transiciones.add(new_trans_trampa);
                    	}
                    }
                }
                if(haytrampa){
                    new_estados.add(estado_Trampa);
                    new_est_final.add(estado_Trampa);
	                Iterator<Character> iterator_alfabeto = alfabeto.iterator();
	                while(iterator_alfabeto.hasNext()){
	                	Character element_alfabeto = iterator_alfabeto.next();
	                	// agrego la transicion a si mismo para cualquier letra del alfabeto
	                	Triple<State,Character,State> new_trans_trampa = new Triple<State,Character,State>(estado_Trampa,element_alfabeto,estado_Trampa);
	                	new_transiciones.add(new_trans_trampa);                	
	                }
                }
                DFA complemento = new DFA(new_estados, alfabeto, new_transiciones, inicial, new_est_final);
                return complemento;		
	}
	
	/**
	 * Returns a new automaton which recognizes the kleene closure
	 * of language. 
	 * 
	 * @returns a new DFA accepting the language's complement.
	 */
	public DFA star() {
		assert rep_ok();
		// TODO
		return null;		
	}
	
	/**
	 * Returns a new automaton which recognizes the union of both
	 * languages, the one accepted by 'this' and the one represented
	 * by 'other'. 
	 * 
	 * @returns a new DFA accepting the union of both languages.
	 */	
	public DFA union(DFA other) {
		assert rep_ok();
		assert other.rep_ok();
		// TODO	
			//Union de los alfabetos
			Set<Character> union_alfabeto = new HashSet<Character>();
	        union_alfabeto.addAll(alphabet());
	        union_alfabeto.addAll(other.alphabet());
	        //Union de estados
	        Set<State> union_estados = new HashSet<State>();
	        union_estados.addAll(estados);
	        //Union de transiciones
	        Set<Triple<State,Character,State>> union_transitions = new HashSet<Triple<State,Character,State>>();
	        union_transitions.addAll(transiciones);
	        //Union de estados finales
	        Set<State> union_finales = new HashSet<State>();
	        union_finales.addAll(estados_finales);
	        //Nuevo estado inicial
			other.inicial.setName("?"+other.inicial.name().substring(1));
	 
			//renombrar estados del parametro
	        Iterator<State> iterator = other.estados.iterator();
	        while (iterator.hasNext()){
	        	State corriente = iterator.next();
	        	corriente.setName("?"+corriente.name().substring(1));
	        	union_estados.add(corriente);
	        }
	
	        //renombrar transacciones del parametro
	        Iterator<Triple<State, Character, State>> iterator_transiciones = other.transiciones.iterator();
	        while (iterator_transiciones.hasNext()){
	        	Triple<State, Character, State> transicion_corriente = iterator_transiciones.next();
	        	transicion_corriente.first().setName("?"+transicion_corriente.first().name().substring(1));
	        	transicion_corriente.third().setName("?"+transicion_corriente.third().name().substring(1));
	        	union_transitions.add(transicion_corriente);
	        }
	        
	        //renombrar estados finales del parametro
	        Iterator<State> iterator_estados_finales = other.estados_finales.iterator();
	        while (iterator_estados_finales.hasNext()){
	        	State estado_final_corriente = iterator_estados_finales.next();
	        	estado_final_corriente.setName("?"+estado_final_corriente.name().substring(1));
	        	union_finales.add(estado_final_corriente);
	        }
	         
	        //nuevo estado inicial "l0"
	        State nuevo_inicial = new State("l0");
	        union_estados.add(nuevo_inicial);
	        //agregado la transaccion l0->q0 [label="Lambda"]; 
	        //agregado la transaccion l0->?0 [label="Lambda"];       
	        Triple<State, Character, State> aP = new Triple<State, Character, State>(nuevo_inicial, Lambda, other.inicial);
	        Triple<State, Character, State> aQ = new Triple<State, Character, State>(nuevo_inicial, Lambda, inicial);
	 
	        union_transitions.add(aP);
	        union_transitions.add(aQ);                
	        //Nuevo NFALambda con las uniones y un nuevo estado inicial con dos transacciones lambda a los iniciales de los AFD
	        NFALambda nfaLambda = new NFALambda(union_estados, union_alfabeto, union_transitions, nuevo_inicial, union_finales); 
	        
	        DFA dfa = nfaLambda.toDFA();
	        return dfa;
        }
	
	/**
	 * Returns a new automaton which recognizes the intersection of both
	 * languages, the one accepted by 'this' and the one represented
	 * by 'other'. 
	 * 
	 * @returns a new DFA accepting the intersection of both languages.
	 */	
	public DFA intersection(DFA other) {
		assert rep_ok();
		assert other.rep_ok();
		// TODO
		// Es el complemento de la union de los complementos de ambos DFA
		return (complement().union(other.complement())).complement();		
	}
   
	@Override
	public boolean rep_ok() {
		// TODO: Check that the alphabet does not contains lambda.
		// TODO: Check that initial and final states are included in 'states'.
		// TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
		// TODO: Check that the transition relation is deterministic.              
                if ((!alfabeto.contains('_')) && (estados.contains(inicial)) && (checkFinalStates()) && (checkTransition()) && (checkDeterministic())){
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

    private boolean checkDeterministic() {
        Set<Tuple<State,Character>> visitados = new HashSet<>();
        Iterator<Triple<State,Character,State>> iterator = transiciones.iterator();
        while (iterator.hasNext()){
            Triple<State,Character,State> elementTriple = iterator.next();
            Tuple<State,Character> elementTuple = new Tuple<>(elementTriple.first(), elementTriple.second());
            if (visitados.contains(elementTuple)){
                return false;
            }
            visitados.add(elementTuple);
        }
        return true;
    }
    
}
