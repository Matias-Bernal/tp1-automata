package automata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Triple;
import utils.Tuple;

public abstract class FA {

    Set<State> estados;		
    Set<Character> alfabeto;		
    Set<Triple<State,Character,State>> transiciones;		
    State inicial;		
    Set<State> estados_finales;
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
	@SuppressWarnings("resource")
	public static FA parse_form_file(String path) throws Exception {
		// TODO
		FA automata = null;
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		boolean nfaLambda = false;
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
		/////////Eliminar si hay lineas en blanco//////////
		Pattern patBlanco = Pattern.compile("^(\\n|\t|\\s)*$");
		Matcher matBlanco = patBlanco.matcher(line);
		while (matBlanco.matches() && line!=null){
			line = br.readLine();
			matBlanco = patBlanco.matcher(line);
		}
		if(line==null)
			throw new Exception();
		//////////////////////////////////////////////////
		
		Pattern pat = Pattern.compile("^digraph\\s[{]$");
		Matcher mat = pat.matcher(line);
		if (mat.matches()) {
			line = br.readLine();
			/////////Eliminar si hay lineas en blanco//////////
			matBlanco = patBlanco.matcher(line);
			while (matBlanco.matches() && line!=null){
				line = br.readLine();
				matBlanco = patBlanco.matcher(line);
			}
			if(line==null)
				throw new Exception();
			//////////////////////////////////////////////////
			pat = Pattern.compile("^(\\n|\t|\\s)*inic\\[shape=point\\];$");
			mat = pat.matcher(line);
			if (mat.matches()) {
				line = br.readLine();
				/////////Eliminar si hay lineas en blanco//////////
				matBlanco = patBlanco.matcher(line);
				while (matBlanco.matches() && line!=null){
					line = br.readLine();
					matBlanco = patBlanco.matcher(line);
				}
				if(line==null)
					throw new Exception();
				//////////////////////////////////////////////////
				pat = Pattern.compile("^(\\n|\t|\\s)*inic->[a-z�A-Z�]+\\d+;$");
				mat = pat.matcher(line);
				if (mat.matches()) {
					//Guardar el estado Inicial
					initial = new State(line.substring(line.indexOf('q'), line.indexOf(';'))); //cambiar q por [a-z�A-Z�]+\\d+
					if(!states.contains(initial))
						states.add(initial);
					//Guardar los estados y las transiciones
					line = br.readLine();
					/////////Eliminar si hay lineas en blanco//////////
					matBlanco = patBlanco.matcher(line);
					while (matBlanco.matches() && line!=null){
						line = br.readLine();
						matBlanco = patBlanco.matcher(line);
					}
					if(line==null)
						throw new Exception();
					//////////////////////////////////////////////////
					
					Pattern pat1 = Pattern.compile("^(\\n|\t|\\s)*[a-z�A-Z�]+\\d+\\[shape=doublecircle\\];$");
					Matcher mat1 = pat1.matcher(line);
					Pattern pat2 = Pattern.compile("^(\\n|\t|\\s)*[a-z�A-Z�]+\\d+->[a-z�A-Z�]+\\d+\\s\\[label=\".+\"\\];$");
					Matcher mat2 = pat2.matcher(line);
					while(!mat1.matches() && mat2.matches()){ //no son el/los estados finales
						State from = new State(line.substring(line.indexOf('q'), line.indexOf('-')));
						State to = new State(line.substring(line.indexOf(">")+1, line.indexOf('[')-1));
						if(!states.contains(from))
							states.add(from);
						if(!states.contains(to))
							states.add(to);
							
						Vector<Character> labels = new Vector<Character>();
						if((line.substring(line.indexOf('"')+1, line.indexOf(']')-1)).length()>1){
							String[] labelsStr = (line.substring(line.indexOf('\"'), line.indexOf(']')-1)).split(",");
							for(int i=0;i<labelsStr.length;i++){
								Character clabel = labelsStr[i].charAt(0);
								labels.add(clabel);
							}
						}else{
							Character clabel = (line.substring(line.indexOf('"')+1, line.indexOf(']')-1)).charAt(0);
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
						}
						line = br.readLine();
						/////////Eliminar si hay lineas en blanco//////////
						matBlanco = patBlanco.matcher(line);
						while (matBlanco.matches() && line!=null){
							line = br.readLine();
							matBlanco = patBlanco.matcher(line);
						}
						if(line==null)
							throw new Exception();
						//////////////////////////////////////////////////
						mat1 = pat1.matcher(line);
						mat2 = pat2.matcher(line);

					}
					//line=br.readLine(); //linea en blanco antes de los estados finales
					//Guarda el primer de el/los estado/s final/es
					final_state = new State(line.substring(line.indexOf('q'), line.indexOf('[')));
					if(!states.contains(final_state))
						states.add(final_state);
					final_states.add(final_state);
						
					line=br.readLine();
					/////////Eliminar si hay lineas en blanco//////////
					matBlanco = patBlanco.matcher(line);
					while (matBlanco.matches() && line!=null){
						line = br.readLine();
						matBlanco = patBlanco.matcher(line);
					}
					if(line==null)
						throw new Exception();
					//////////////////////////////////////////////////
					pat = Pattern.compile("^(\\n|\t|\\s*)[a-z�A-Z�]+\\d+\\[shape=doublecircle\\];$");
					while(mat.matches() || line!=null){ //Guardar el/los estados finales
						mat = pat.matcher(line);
						if(mat.matches()){ //Es estado final
							final_state = new State(line.substring(line.indexOf('q'), line.indexOf('[')));
							if(!states.contains(final_state))
								states.add(final_state);
							final_states.add(final_state);
							line=br.readLine();
							/////////Eliminar si hay lineas en blanco//////////
							matBlanco = patBlanco.matcher(line);
							while (matBlanco.matches() && line!=null){
								line = br.readLine();
								matBlanco = patBlanco.matcher(line);
							}
							if(line==null)
								throw new Exception();
							//////////////////////////////////////////////////
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
								automata = new NFALambda(states, alphabet, transitions, initial, final_states);
							}else{
								assert !(states.isEmpty());
								assert !(transitions.isEmpty());
								assert !(alphabet.isEmpty());
				                Iterator<Triple<State,Character,State>> iterator = transitions.iterator();
				                Set<Tuple<State,Character>> visitados = new HashSet<Tuple<State,Character>>();
				                while (iterator.hasNext()){
				                    Triple<State,Character,State> elementTriple = iterator.next();
				                    Tuple<State,Character> elementTuple = new Tuple<State,Character>(elementTriple.first(),elementTriple.second());
				                    if (visitados.contains(elementTuple)){
				                    	nfa = true;
				                        break;
				                    }
				                    visitados.add(elementTuple);
				                }
				                if(!nfa){
									automata = new DFA(states, alphabet, transitions, initial, final_states);
								}else{
									automata = new NFA(states, alphabet, transitions, initial, final_states);
								}
							}
						}else{
							throw new Exception();
						}									
					}else{
						throw new Exception();
					}
				}
			}else{
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
		int i = 0;
                while (i<s.length() && alfabeto.contains(s.charAt(i))){
                    i++;
                }
                if (i< s.length()){
                    return false;
                }
                return true;
	}
	
	/**
	 * @return True iff the automaton is in a consistent state.
	 */
	public abstract boolean rep_ok();
	
}
