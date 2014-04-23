package automata;

import java.util.*;
import utils.Triple;

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
		return null;
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
		return null;
	}
	
	/**
	 * Converts the automaton to a NFALambda.
	 * 
	 * @return NFALambda recognizing the same language.
	 */
	public NFALambda toNFALambda() {
		assert rep_ok();
		// TODO
		return null;
	}

	/**
	 * Checks the automaton for language emptiness.
	 * 
	 * @returns True iff the automaton's language is empty.
	 */
	public boolean is_empty() {
		assert rep_ok();
		// TODO            
                if(estados.isEmpty() || inicial==null || transiciones.isEmpty() || !is_finite()){
                    return true;
                }
                if(estados_finales.contains(inicial)){
                    return false;
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
            Set<State> visitados = new HashSet();
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
		return null;		
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
		return null;		
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
		return true;
	}


	

}