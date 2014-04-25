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
                State state = delta(inicial, string.charAt(0));
                if (state == null) {return false;}
                for(int i=1; i<string.length(); i++){
                    state = delta(state, string.charAt(i));
                    if (state == null) {return false;}
                }
		return estados_finales.contains(state);
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
		boolean res = false;
        if(estados.isEmpty() || inicial==null || transiciones.isEmpty() ){
            res = true;
        }
        if(estados_finales.contains(inicial)){
            res = false;
        }	
		return res;
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
                // agrego el estado trampa a los estados y a lo hago estado final
                State estado_Trampa = new State("trampa");
                new_estados.addAll(estados);
                new_estados.add(estado_Trampa);
                new_est_final.add(estado_Trampa);
                //agrego todas las transiciones al estado trampa que hacen falta para que sea completo
                new_transiciones.addAll(transiciones);
                Iterator<Character> iterator_alfabeto = alfabeto.iterator();
                while(iterator_alfabeto.hasNext()){
                    Character element_alfabeto = iterator_alfabeto.next();
                    //agrego las transiciones que no estan al estado trampa
                    Iterator<Triple<State,Character,State>> iterator_transiciones = transiciones.iterator();
                    while(iterator_transiciones.hasNext()){
                        Triple<State,Character,State> element_transiciones = iterator_transiciones.next();
                        if (delta(element_transiciones.first(),element_alfabeto)== null){
                            Triple<State,Character,State> new_trans_trampa = new Triple<>(element_transiciones.first(),element_alfabeto,estado_Trampa);
                            new_transiciones.add(new_trans_trampa);
                        }
                    }
                    // agrego la transicion a si mismo para cualquier letra del alfabeto
                    Triple<State,Character,State> new_trans_trampa = new Triple<>(estado_Trampa,element_alfabeto,estado_Trampa);
                    new_transiciones.add(new_trans_trampa);
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
                //creo los estados, las transiciones, los estados finales, el estado inicial y el alfabeto de la union.
                Set<Tuple<State,State>> union_estados = new HashSet<Tuple<State,State>>();
                Set<Triple<Tuple<State,State>,Character,Tuple<State,State>>> union_transiciones = new HashSet<>();
                Set<Tuple<State,State>> union_est_finales = new HashSet<Tuple<State,State>>();
                Tuple<State,State> union_inicial = new Tuple<State, State>(inicial, other.initial_state());
                Set<Character> union_alfabeto = new HashSet<Character>();
                union_alfabeto.addAll(this.alfabeto);
                union_alfabeto.addAll(other.alphabet());
                
                //obtengo los estados de la union        
                Iterator<State> estado_iterator = this.estados.iterator();
                while(estado_iterator.hasNext()){
                    State element = estado_iterator.next();
                    Iterator<State> other_est_iterator = other.states().iterator();
                    while(other_est_iterator.hasNext()){
                        State other_element = other_est_iterator.next();
                        Tuple<State,State> union_estado = new Tuple<>(element,other_element);
                        union_estados.add(union_estado);    
                    }
                }
                
                //obtengo los estados finales y las transiciones de la union
                Iterator<Tuple<State,State>> union_estado_iterator = union_estados.iterator();
                while(union_estado_iterator.hasNext()){
                    Tuple<State,State> union_element = union_estado_iterator.next();
                    //obtengo los estados finales de la union
                    boolean containFirst = estados_finales.contains(union_element.first());
                    boolean containSecond = other.final_states().contains(union_element.second());
                    if (containFirst || containSecond){
                        union_est_finales.add(union_element);
                    }
                    //obtengo las transiciones de la union
                    Iterator<Character> alfabeto_iterator = union_alfabeto.iterator();
                    while(alfabeto_iterator.hasNext()){
                        Character c = alfabeto_iterator.next();
                        State first = delta(union_element.first(), c);
                        State second = delta(union_element.second(), c);
                        Tuple<State,State> element_trans = new Tuple<>(first, second);
                        Triple<Tuple<State,State>,Character,Tuple<State,State>> union_trans = new Triple<>(union_element, c, element_trans); 
                        union_transiciones.add(union_trans);
                    }
                
                // falta renombrar cada tuple<estado, estado> con un estado  
                }
                Set<State> new_estados = new HashSet<State>();
                Set<State> new_estados_finales = new HashSet<State>();
                int i = 0;
                State newinitial = new State("s"+i);
                new_estados.add(newinitial);
                i++;
                Set<Tuple<Tuple<State,State>,State>> estados_Guardados = new HashSet<Tuple<Tuple<State,State>,State>>(); 
                estados_Guardados.add(new Tuple<Tuple<State,State>, State>(union_inicial, newinitial));
               
                if(union_est_finales.contains(union_inicial))
                	new_estados_finales.add(newinitial);
                
                Set<Triple<State,Character,State>> transiciones_Guardadas = new HashSet<Triple<State,Character,State>>();
                Iterator<Triple<Tuple<State, State>, Character, Tuple<State, State>>> uniontransiciones = union_transiciones.iterator();
                
                while(uniontransiciones.hasNext()){
                  Triple<Tuple<State, State>, Character, Tuple<State, State>> transition_element = uniontransiciones.next();
                  State newFromState = devolverGuardado(estados_Guardados, transition_element.first());  
                  if(newFromState==null){
                	  newFromState = new State("s"+i);
                      new_estados.add(newFromState);
                      i++;
                      estados_Guardados.add(new Tuple<Tuple<State,State>, State>(transition_element.first(),newFromState));
                  }
                  if(new_estados_finales.contains(transition_element.first()))
                	  new_estados_finales.add(newFromState);
                  
                  State newToState = devolverGuardado(estados_Guardados, transition_element.third());
                  if(newToState==null){
                	  newToState = new State("s"+i);
                      new_estados.add(newToState);
                      i++;
                      estados_Guardados.add(new Tuple<Tuple<State,State>, State>(transition_element.third(),newToState));
                  }
                  
                  if(new_estados_finales.contains(transition_element.third()))
                	  new_estados_finales.add(newFromState);
                  
                  Triple<State,Character,State> newTransition = new Triple<State, Character, State>(newFromState, transition_element.second(),  newToState);
                  if(!transiciones_Guardadas.contains(newTransition)){
                	  transiciones_Guardadas.add(newTransition);
                  }
                  
                }
                DFA dfa = new DFA(new_estados, union_alfabeto,transiciones_Guardadas,newinitial,new_estados_finales);
                return dfa;
        }
	
	
	public State devolverGuardado(Set<Tuple<Tuple<State,State>,State>> conjunto,Tuple<State,State> tupla){
		State res = null;
		Iterator<Tuple<Tuple<State, State>, State>> iterator = conjunto.iterator();
        while(iterator.hasNext()){
          Tuple<Tuple<State, State>, State> element = iterator.next();
          if(element.first().equals(tupla))
        	  return element.second();
        }
		return res;
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
		return null;		
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
