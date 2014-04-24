package automata;

import java.util.*;

import utils.Triple;

public class NFA extends FA {

	/*
	 *  Construction
	*/
	
	// Constructor
	public NFA(	Set<State> states,
			Set<Character> alphabet,
			Set<Triple<State,Character,State>> transitions,
			State initial, 
			Set<State> final_states)
	throws IllegalArgumentException
	{
		this.alfabeto = alphabet;
                this.estados = states;
                this.estados_finales = final_states;
                this.inicial = initial;
                this.transiciones = transitions;
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
                //TODO
                Set <State> states_successors = new HashSet();
                Stack succ = new Stack();
                succ.addAll(delta(inicial,string.charAt(0)));
                boolean match;
               // while(!match){
                //    macheo();
                //}
                //result match;
                //states_successors = successors(inicial,string);
               
                //st.addAll(states_successors);
                //states_successors.clear();
                
          /*      while(!st.empty()){
                    State elem = (State) st.pop();
                    if(!visitados.contains(elem)){
                        visitados.add(elem);
                    }else{return false;}
                    state_successors = successors(elem);
                    st.addAll(state_successors);
                    state_successors.clear();
                }
                //while(iterator.hasNext()){
                  //  Triple<State,Character,State> element = iterator.next();
                    
                    //if(visitados.contains(element.third())){
                      //  visitados.add(element.third());
                    //}else{return false;}
        //        }*/
		return true;
	}
        
    
	/**
	 * Converts the automaton to a DFA.
	 * 
	 * @return DFA recognizing the same language.
	*/
	public DFA toDFA() {
		assert rep_ok();
		// TODO           
		return null;
	}


	@Override
	public boolean rep_ok() {
		// TODO: Check that the alphabet does not contains lambda.
		// TODO: Check that initial and final states are included in 'states'.
		// TODO: Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
		if ((!alfabeto.contains('_')) && (estados.contains(inicial)) && (checkFinalStates()) && (checkTransition())){
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
