package automata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Triple;

public abstract class FA {

	public static final Character Lambda = '_';
	
	/* Creation */
	
	/**	Parses and returns a finite automaton form the given file. The type of
	 * the automaton returned is the appropriate one for the automaton represented
	 * in the file (i.e. if the file contains the representation of an 
	 * automaton that is non-deterministic but has no lambda transitions, then an
	 * instance of NFA must be returned).
	 * 
	 * @param path Path to the file containing the specification of an FA.
	 * @return An instance of DFA, NFA or NFALambda, corresponding to the automaton
	 * represented in the file.
	 * @throws Exception Throws an exception if there is an error during the parsing process.
	 */
	public static FA parse_form_file(String path) throws Exception {
		// TODO
		FA automata = null;
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		boolean nfaLambda = false;
		boolean dfa = false;
		boolean nfa = false;
		Set<State> states = new HashSet<State>();
		Set<Character> alphabet = new HashSet<Character>();
		Set<Triple<State,Character,State>> transitions = new HashSet<Triple<State,Character,State>>();
		State initial;
		Set<State> final_states = new HashSet<State>();
		State final_state;
		//Abrir el archivo
		file = new File (path);
		fr = new FileReader (file);
		br = new BufferedReader(fr);
		//Comprobar y Armar el Automata
		String line = br.readLine();
		Pattern pat = Pattern.compile("^digraph\\s[{]$");
		Matcher mat = pat.matcher(line);
		if (mat.matches()) {
			line = br.readLine();
			pat = Pattern.compile("^(\\n|\t|\\s)*inic\\[shape=point\\];$");
			mat = pat.matcher(line);
			if (mat.matches()) {
				line = br.readLine();
				pat = Pattern.compile("^(\\n|\t|\\s)*inic->[a-zñA-ZÑ]+\\d+;$");
				mat = pat.matcher(line);
				if (mat.matches()) {
					//Guardar el estado Inicial
					initial = new State(line.substring(line.indexOf('q'), line.indexOf(';'))); //cambiar q por [a-zñA-ZÑ]+\\d+
					states.add(initial);
					//Guardar los estados y las transiciones
					line = br.readLine();
					if(line!=null){
						pat = Pattern.compile("^(\\n|\t|\\s)*[a-zñA-ZÑ]+\\d+\\[shape=doublecircle\\];$");
						mat = pat.matcher(line);
						Pattern pat2 = Pattern.compile("^(\\n|\t|\\s)*[a-zñA-ZÑ]+\\d+->[a-zñA-ZÑ]+\\d+\\s\\[label=\".+\"\\];$");
						Matcher mat2 = pat2.matcher(line);
						while(!mat.matches() && mat2.matches()){ //no son el/los estados finales
							State from = new State(line.substring(line.indexOf('q'), line.indexOf('-')));
							State to = new State(line.substring(line.indexOf('>')+1, line.indexOf('\b')));
							if(!states.contains(from))
								states.add(from);
							if(!states.contains(to))
								states.add(to);
							
							Vector<Character> labels = new Vector<Character>();
							if((line.substring(line.indexOf('"'), line.indexOf(']')-1)).length()>1){
								String[] labelsStr = (line.substring(line.indexOf('"'), line.indexOf(']')-1)).split(",");
								for(int i=0;i<labelsStr.length;i++){
									Character clabel = labelsStr[i].charAt(0);
									labels.add(clabel);
								}
							}else{
								Character clabel = line.substring(line.indexOf('"'), line.indexOf(']')-1).charAt(0);
								labels.add(clabel);
							}
							for(int i=0; i<labels.size();i++){
								Character label = labels.elementAt(i);
								if(label.equals(Lambda))
									nfaLambda |= true;
								if(!alphabet.contains(label))
									alphabet.add(label);
								Triple<State,Character,State> transition = new Triple<State, Character, State>(from, label, to);
								if(!transitions.contains(transition))
									transitions.add(transition);
								
								//FALTA VER SI ES DFA O NFA
								
								
							}
							line = br.readLine();
							mat = pat.matcher(line);
							mat2 = pat2.matcher(line);
						}
						//Guarda el primer de el/los estado/s final/es
						final_state = new State(line.substring(line.indexOf('q'), line.indexOf('[')));
						final_states.add(final_state);
						
						line=br.readLine();
						pat = Pattern.compile("^(\\n|\t|\\s*)[a-zñA-ZÑ]+\\d+\\[shape=doublecircle\\];$");
						while(mat.matches() || line!=null){ //Guardar el/los estados finales
							mat = pat.matcher(line);
							if(mat.matches()){ //Es estado final
								final_state = new State(line.substring(line.indexOf('q'), line.indexOf('[')));
								final_states.add(final_state);
								line=br.readLine();
							}else{
								break;
							}
						}
						//la ultima linea
						if(line!=null){
							pat = Pattern.compile("^(\\n|\t|\\s*)[}]$");
							mat = pat.matcher(line);
							if(mat.matches()){ // la ultima es el }
								//crea la instancia correcta de automata para retornar
								if(nfaLambda){
									automata = new NFALambda(final_states, alphabet, transitions, initial, final_states);
								}else{
									if(dfa){
										automata = new DFA(states, alphabet, transitions, initial, final_states);
									}else{
										if(nfa){
											automata = new NFA(states, alphabet, transitions, initial, final_states);
										}		
									}
								}
							}else{
								throw new Exception();
							}									
						}else{
							throw new Exception();
						}
					}else{
						throw new Exception();
					}
				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}
		} else {
			throw new Exception();
		}
		return automata;
	}
	
	/*
	 * 	State Querying
	*/
	
	/**
	 * @return the atomaton's set of states.
	 */
	public abstract Set<State> states();
	
	/**
	 * @return the atomaton's alphabet.
	 */
	public abstract Set<Character> alphabet();
	
	/**
	 * @return the atomaton's initial state.
	 */
	public abstract State initial_state();
	
	/**
	 * @return the atomaton's final states.
	 */
	public abstract Set<State> final_states();
	
	/**
	 * Query for the automaton's transition function.
	 * 
	 * @return A state or a set of states (depending on whether the automaton 
	 * is a DFA, NFA or NFALambda) corresponding to the successors of the given
	 * state via the given character according to the transition function.
	 */
	public abstract Object delta(State from, Character c);
	
	/**
	 * @return Returns the DOT code representing the automaton.
	 */	
	public abstract String to_dot();
	
	
	/*
	 * 	Automata Methods 
	*/
	
	
	/**
	 * Tests whether a string belongs to the language of the current 
	 * finite automaton.
	 * 
	 * @param string String to be tested for acceptance.
	 * @return Returns true iff the current automaton accepts the given string.
	 */
	public abstract boolean accepts(String string);
	
	/**
	 * Verifies whether the string is composed of characters in the alphabet of the automaton.
	 * 
	 * @return True iff the string consists only of characters in the alphabet.
	 */
	public boolean verify_string(String s) {
		// TODO
		return false;
	}
	
	/**
	 * @return True iff the automaton is in a consistent state.
	 */
	public abstract boolean rep_ok();
	
}
