package automata;

import java.io.*;
import java.util.*;

import utils.ExpresionRegular;
import utils.Triple;
import utils.Tuple;

/* Implements a DFA (Deterministic Finite Atomaton).
*/
public class DFA extends FA {

	/** DFA(): Contructor con parametros de la clase DFA
	 * <li>pre: true </li>
	 * <li>post: DFA </li>
	 * <hr>
	 * @param states
         * @param alphabet 
         * @param transitions
         * @param initial
         * @param final_states
	 * @return ADF
	 * @throws IllegalArgumentException
	 */
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
	
        
        
        /** delta(): Funcion que retorna un estado alcanzable desde un estado from a travez de un caracter c
	 * <li>pre: state!= null and character!=null </li>
	 * <li>post:  </li>
	 * <hr>
	 * @param states
         * @param c
	 * @return State or null
	 */
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
	
        
        /** to_dot(): Funcion que traduce un DFA al formato dot para luego ser impreso.
	 * <li>pre: true </li>
	 * <li>post:  </li>
	 * <hr>
	 * @return String
	 */
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
	
        /** accepts(): Metodo de acceptacion de una cadena en un DFA
	 * <li>pre: string </li>
	 * <li>post:  </li>
	 * <hr>
         * @param string
	 * @return Boolean
	 */	
	@Override
	public boolean accepts(String string) {
        assert rep_ok();
		assert string != null;
		assert verify_string(string);
		// TODO
			if(string.isEmpty()){
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

        /** toNFA(): Converts the automaton to a NFA.
	 * <li>pre: true </li>
	 * <li>post:  </li>
	 * <hr>
	 * @return NFA recognizing the same language.
	 */
	public NFA toNFA() {
		assert rep_ok();
		// TODO
		NFA nfaAutomata = new NFA(estados, alfabeto, transiciones, inicial, estados_finales);
		return nfaAutomata;
	}
	
        /** toNFALambda(): Converts the automaton to a NFALambda.
	 * <li>pre: true </li>
	 * <li>post:  </li>
	 * <hr>
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

        
        /** is_empty(): Checks the automaton for language emptiness.
	 * <li>pre: true </li>
	 * <li>post:  </li>
	 * <hr>
	 * @return True iff the automaton's language is empty.
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
        while (!res){
            if (sucesor2.isEmpty()){
                sucesor2.addAll(sucesores(sucesor1, transiciones));
                sucesor1.clear();
                if(sucesor2.isEmpty()){
                    return false;
                }else{
                    res = containFinal(sucesor2, estados_finales);
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
        }
        return true;
	}
   
        
    /** sucesores(): funcion que a base de un set de estados y un arreglo 
     * de transiciones, devuelve un set de sucesores de todos los estados ingresados como parametro
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param estados 
     * @param transiciones 
     * @return result
    */ 
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
        
    /** containFinal(): //funcion que cheque si un set contiene por lo menos un estado final
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param states
     * @param finals 
     * @return boolean
    */ 
    
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
        
    /** is_finite():Checks the automaton for language infinity.
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param states
     * @param finals 
     * @returns True iff the automaton's language is finite.
    */
    public boolean is_finite() {
        assert rep_ok();
        boolean resp = true;
            
        //Veo si los sucesores de un estado contiene a dicho estado            
        Iterator<State> iteratorState = estados.iterator();
        while(iteratorState.hasNext()){
            Set<State> visitados = new HashSet<State>();
            State currentState = iteratorState.next();
            visitados.add(currentState);
            if (sucesoresContineEstado(visitados,currentState)){
                resp = false;
            		break;
            }
        }
        return resp; 
    }

    /** sucesoresContineEstado():
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param visitados
     * @param estado
     * @returns boolean
    */
    public boolean sucesoresContineEstado(Set<State> visitados, State estado){
        boolean resp = false;
        Iterator<Triple<State,Character,State>> iterator = transiciones.iterator();
        while(iterator.hasNext()){
            Triple<State,Character,State> element = iterator.next();
            if(element.first().equals(estado)){
                if(element.third().equals(estado)){
                    resp = false;
                    break;
                }else{
                    visitados.add(estado);
                    if(!visitados.contains(element.third())){
                        visitados.add(element.third());
                        if(sucesoresContineEstado(visitados,element.third())){
                            resp = true;
                            break;
                        }
                    }else{
                        resp = true;
                        break;
                    }    			
                }
            }
        }
        return resp;
    }
    
    /** complemento():Returns a new automaton which recognizes the complementary language. 
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
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
	
    /** star():
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @returns DFA
    */
    public DFA star() {
        assert rep_ok();
        Iterator<State> iterator_estados_finales = estados_finales.iterator();
        Set<Triple<State,Character,State>> new_transitions = new HashSet<Triple<State,Character,State>>();
        new_transitions.addAll(transiciones);
        while (iterator_estados_finales.hasNext()){
        	State current_final = iterator_estados_finales.next();
        	Triple<State,Character,State> new_transition = new Triple<State, Character, State>(current_final, FA.Lambda, inicial);
        	new_transitions.add(new_transition);
        }
        NFALambda nfalambda = new NFALambda(estados, alfabeto, new_transitions, inicial, estados_finales);
		return nfalambda.toDFA();
	}
	
    
    /** union(): Returns a new automaton which recognizes the union of both
     * languages, the one accepted by 'this' and the one represented
     * by 'other'. 
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @returns a new DFA accepting the union of both languages.
    */
    public DFA union(DFA other) {
		assert rep_ok();
		assert other.rep_ok();
		// TODO	
                
        //renombro el automata other para que no me genere conflictos de nombres
        other = renombrar(other,'r');
        
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

        
        union_estados.addAll(other.states());
        union_transitions.addAll(other.transiciones);
        union_finales.addAll(other.final_states());
            
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

        
    /** intersection():Returns a new automaton which recognizes the intersection of both
     * languages, the one accepted by 'this' and the one represented
     * by 'other'. 
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param other
     * @returns a new DFA accepting the intersection of both languages.
    */
    public DFA intersection(DFA other) {
            assert rep_ok();
            assert other.rep_ok();
            // TODO

        //renombro el automata other para que no me genere conflictos de nombres
        other = renombrar(other,'r');
        
        //creo el alfabeto de la interseccion
        Set<Character> interseccion_alfabeto = new HashSet<Character>();
        interseccion_alfabeto.addAll(this.alfabeto);
        interseccion_alfabeto.addAll(other.alphabet());
        interseccion_alfabeto.add(FA.Lambda);

        // creo todos los estados de la interseccion
        Set<State> interseccion_estados = new HashSet<State>();
        // creo un set con los estados del automata corriente sin los estados finales ya que queradan sin direccionar
        interseccion_estados.addAll(estados);
        interseccion_estados.addAll(other.estados);
        // el nuevo estado inicial
        State interseccion_estado_inicial = inicial;
        // los nuevos estados finales
        Set<State> interseccion_estados_finales = new HashSet<State>();
        interseccion_estados_finales.addAll(other.estados_finales);
        // las nuevas transiciones       
        Set<Triple<State,Character,State>> interseccionTransiciones = new HashSet<Triple<State,Character,State>>();
        interseccionTransiciones.addAll(transiciones);
        interseccionTransiciones.addAll(other.transiciones);

        Iterator<State> iterador_estados_finales = estados_finales.iterator();
        while (iterador_estados_finales.hasNext()){
            State estado_final = iterador_estados_finales.next();
            Triple<State,Character,State> transition_lambda = new Triple<State, Character, State>(estado_final, FA.Lambda, other.inicial);            
            interseccionTransiciones.add(transition_lambda);
        }
   
        //Creo un DFA de la interseccion como resultado.
        NFALambda automata = new NFALambda(interseccion_estados, interseccion_alfabeto, interseccionTransiciones, interseccion_estado_inicial, interseccion_estados_finales);
        return automata.toDFA();
        
        // Es el complemento de la union de los complementos de ambos DFA
        // return (complement().union(other.complement())).complement();
	}
   
   /** renombrar(): Funcion que a base de un automata le renombra todos los 
     * estados, las transiciones, los estados finales y el estado inicial
     * no remobra el arfabeto ya que no afecta al funcionamiento con otros automatas
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param other
     * @param a
     * @returns DFA
    */
        private DFA renombrar(DFA other, Character a){
            //Nuevo estado inicial
        	
        	State new_inicial = new State(a+other.inicial.name().substring(1));
        	
        	Set<Character> new_alfabet = new HashSet<Character>();
        	new_alfabet.addAll(other.alfabeto);
        	
        	Set<State> new_estados = new HashSet<State>();
        	Set<State> new_estados_finales = new HashSet<State>();
        	
        	new_estados.add(new_inicial);
            
        	if(other.estados_finales.contains(other.inicial))
        		new_estados_finales.add(new_inicial);
	        //renombrar transacciones del parametro
	        Iterator<Triple<State, Character, State>> iterator_transiciones = other.transiciones.iterator();
	        Set<Triple<State, Character, State>> new_transitions = new HashSet<Triple<State, Character, State>>();
	        

	        while (iterator_transiciones.hasNext()){
	        	Triple<State, Character, State> transicion_corriente = iterator_transiciones.next();
	        	State from_state = new State(a+transicion_corriente.first().name().substring(1));
	        	if(!new_estados.contains(from_state))
	        		new_estados.add(from_state);
	        	if(other.final_states().contains(transicion_corriente.first()))
	        		new_estados_finales.add(from_state);
	        	State to_state = new State(a+transicion_corriente.third().name().substring(1));
	        	if(!new_estados.contains(to_state))
	        		new_estados.add(to_state);
	        	if(other.final_states().contains(transicion_corriente.third()))
	        		new_estados_finales.add(to_state);
	        	Triple<State, Character, State> new_transition = new Triple<State, Character, State>(from_state, transicion_corriente.second(), to_state);
	        	new_transitions.add(new_transition);
	        }
	        DFA automata = new DFA(new_estados, new_alfabet, new_transitions, new_inicial, new_estados_finales);
            return automata;
        }
        
      
   /** rep_ok():
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @returns boolean
    */
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

    /** checkFinalStates(): Funcion que chequea que los estados finales enten 
     * dentro de los estados del automata
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @returns boolean
    */
    private boolean checkFinalStates() {
        Iterator<State> iterator = estados_finales.iterator();
        while (iterator.hasNext()){
            if (!estados.contains(iterator.next())){
                return false;
            }
        }
        return true;
    }

    
    /** checkTransition(): Funcion que chequea que los estados de partida y de 
     * llegada esten dentro del set de estados del automata 
     * y chequea que el caracter este dentro del arfabeto    
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @returns boolean
    */

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

    
    /** checkDeterministic(): Funcion que chequea que a base de un estado y un 
     * caracter no pueda ir a otro estado, o sea que sea un automata determinista
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @returns boolean
    */
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
    
    
    /** minimizeDFA(): Funcion que retorna la minimizacion del automata corriente   
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param automat
     * @returns DFA
    */
    public DFA minimizeDFA (DFA automat){
        assert rep_ok();
        //creo la tabla de estados equivalentes en el cual en el primer elemento tengo la fila, 
        //en el segundo la columna y en el tercero si son equivalentes los estados
        Triple min[][] = new Triple[this.estados.size()-1][this.estados.size()-1];
        
        //paso del set de estados a un arreglo de estados para mayor manejo
        State _estados[] = new State[this.estados.size()];
        _estados = to_Array(this.estados);
        
        //lleno la tabla con la fila y la columna, y agrego una "x" si en la fila o columna existe un estado final
        // si son los dos finales(fila y columna) lo dejo con vacio
        for(int i=0; i<min.length; i++){
            for(int j=0; j<=i; j++){
                if(xor(this.estados_finales.contains(_estados[i+1] ),this.estados_finales.contains(_estados[j]))){
                    min[i][j] = new Triple(_estados[i+1], _estados[j],"x");
                }else{
                    min[i][j] = new Triple(_estados[i+1], _estados[j],"");
                }           
            }
        }
        
        //empieza el algoritmo de llenado de la tabla
        boolean change = true;
        boolean result = false;
        //ciclo hasta que en la tabla no se produzcan cambios 
        while(change){
            change = false;
            //recorre todas las filas
            for(int i=0; i<min.length; i++){
                //recorre todas las columnas
                for(int j=0; j<=i; j++){
                    //comprueba si en una interseccion existe un espacio en blanco como tercera componente
                    if(min[i][j].third()==""){
                        //busca la interseccion de todo los estados siguientes con todo el alfabeto
                        Iterator<Character> iterator_aphabet = alfabeto.iterator();
                        while(!result || iterator_aphabet.hasNext()){
                            Character _char = iterator_aphabet.next();
                            result = deltaMin((State)min[i][j].first(),(State)min[i][j].second(),_char,min);
                        }
                        //si alguno de esas intersecciones posee una x
                        if(result){
                            //cambia la tercera componente de la interseccion por una x
                            min[i][j].setThird("x");
                            change = true;
                        }
                    }
                }
            }
        }
        
        // empieza la creacion del automata a partir de la tabla de estados equivalentes
        Set<Triple<State,Character,State>> _transitions = new HashSet<Triple<State,Character,State>>();
        Set<State> _state_final = new HashSet<State>();
        Set<State> _state = new HashSet<State>();
        //recorro todas las filas
        for(int i=0; i<min.length; i++){
            //recorro todas las columnas
            for(int j=0; j<=i; j++){
                //si encuentro una interseccion con la tercera componente vacia entonces debo unir los estados y renombrar
                //todas las apariciones de los estado de la interseccion por el estado que contiene a los dos estados
                if(min[i][j].third()==""){
                    //creo el nuevo estado que contendra el nombre de ambos
                    State _new = new State((((State)min[i][j].first()).name())+(((State)min[i][j].second()).name()));
                    
                    //empiezo a renombrar las apariciones de los estados en las transiciones
                    _transitions.addAll(transiciones);
                    Iterator <Triple<State,Character,State>> _trans = _transitions.iterator();
                    while(_trans.hasNext()){
                        Triple<State,Character,State> corriente = _trans.next();
                        if(corriente.first().equals((((State)min[i][j].first()))) || (corriente.first().equals((((State)min[i][j].second()))))){
                            corriente.setFirst(_new);
                        }else if(corriente.third().equals((((State)min[i][j].first()))) || (corriente.third().equals((((State)min[i][j].second()))))){
                            corriente.setThird(_new);
                        }
                    }
                    
                    //renombro las apariciones de los estados en los estados finales
                    _state_final.addAll(this.estados_finales);
                    Iterator <State> _state_fnl = _state_final.iterator();
                    while(_state_fnl.hasNext()){
                        State corriente = _state_fnl.next();
                        if(corriente.equals(min[i][j].first()) || corriente.equals(min[i][j].second())){
                            _state_final.remove(corriente);
                            _state_final.add(_new);
                        }
                    }
                    
                    //renombro las apariciones de los estados en el set de estados
                    _state.addAll(this.estados);
                    Iterator <State> _st = _state.iterator();
                    while(_st.hasNext()){
                        State corriente = _st.next();
                        if(corriente.equals(min[i][j].first()) || corriente.equals(min[i][j].second())){
                            _state.remove(corriente);
                            _state.add(_new);
                        }
                    }
                }
            }
        }
        //creo el automata resultante de la minimizacion
        DFA automata = new DFA(_state,this.alfabeto,_transitions,this.inicial, _state_final);
        return automata;
    }
    
    
    /** to_Array(): Funcion que pasa de un set de estados a un arreglo de estados
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param state
     * @returns array de state
    */

    private State[] to_Array(Set<State> state){
        
        Iterator <State> _st = state.iterator();
        State array_state[] = new State[state.size()];
        int i = 0;
        while(_st.hasNext()){
            array_state[i] = _st.next();
            i++;
        }
            return array_state;
    }
    
    /** xor(): Funcion que implementa el xor de booleanos
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param x 
     * @param y 
     * @returns boolean
    */
    private boolean xor(boolean x, boolean y){
        return ( ( x || y ) && ! ( x && y ) );
    }    

    
    /** deltaMin(): obtiene el siguiente de estado_a y de estado_b con el caracter c y despues busca en la matriz la interseccion de esos dos estados siguientes 
     * y se fija si hay una x si no hay entonces la interseccion estado_a y estado _b queda igual, si hay se le pone una x.
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param c 
     * @param estado_a 
     * @param estado_b 
     * @param matriz 
     * @returns boolean
    */
    
    private boolean deltaMin(State estado_a, State estado_b, Character c, Triple[][] matriz){
        State estado1 = delta(estado_a, c);
        State estado2 = delta(estado_b, c);
        int j = 0;
        int i = 0;
        
        //recorro las filas en busca de la fila estado1
        while((i<matriz.length) && (matriz[i][j].first()!= estado1)){
            i++;
        }
        //una vez que ya encontre la fila estado1 busco la columna estado2
        while(j<matriz.length && matriz[i][j]!=null && matriz[i][j].second() != estado2){
            j++;
        }
        //si no encontre la interseccion fila estado1 y columna estado2, intercambio la fila por la columna y busco de nuevo
        if(j==matriz.length || matriz[i][j]==null){
            i = 0;
            j = 0;
            //recorro las filas en busca de la fila estado2
            while((i<matriz.length) && (matriz[i][j].first()!= estado2)){
                i++;
            }
            //una vez que ya encontre la fila estado2 busco la columna estado1
            while(j<matriz.length && matriz[i][j]!=null && matriz[i][j].second() != estado1){
                j++;
            }
        }
        //una vez encontrado me fijo en la tercera componente de la interseccion si posee una x
        return matriz[i][j].third()=="x";
    }
        
   
    
    /** equalsAutomat(): Funcion  que retorna un booleano dependiendo si los 
     * dos automatas son equivalentes o iguales
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param A
     * @param B
     * @returns boolean
    */
   public boolean equalsAutomat(DFA A, DFA B){
       assert rep_ok();
       assert A.rep_ok();
       assert B.rep_ok();
       
       boolean equals = true;
       
       //El mismo alfabeto
       Set<Character> alf_A = new HashSet<>();
       alf_A.addAll(A.alfabeto);
       Iterator<Character> alphabet_A = alf_A.iterator();
       
       Set<Character> alf_B = new HashSet<>();
       alf_B.addAll(B.alfabeto);
       Iterator<Character> alphabet_B = alf_B.iterator();
       
       while(alphabet_A.hasNext() && alphabet_B.hasNext() && equals){
           Character current_A = alphabet_A.next();
           Character current_B = alphabet_B.next();
           if(!current_A.equals(current_B)){
               equals = false;
           }
       }
       
       //Los mismos estados
       Set<State> st_A = new HashSet<>();
       st_A.addAll(A.estados);
       Iterator<State> state_A = st_A.iterator();
       
       Set<State> st_B = new HashSet<>();
       st_B.addAll(B.estados);
       Iterator<State> state_B = st_B.iterator();
       
       while(state_A.hasNext() && state_B.hasNext() && equals){
           State current_A = state_A.next();
           State current_B = state_B.next();
           if(!current_A.equals(current_B)){
               equals = false;
           }
       }
       
       //El mismo numero de transiciones y si las transiciones son las mismas
       Set<Triple<State,Character,State>> transit_A = new HashSet<>();
       transit_A.addAll(A.transiciones);
       Iterator<Triple<State,Character,State>> transitions_A = transit_A.iterator();
       
       Set<Triple<State,Character,State>> transit_B = new HashSet<>();
       transit_B.addAll(B.transiciones);
       Iterator<Triple<State,Character,State>> transitions_B = transit_B.iterator();
       if(!transit_A.containsAll(transit_B)){
           equals = false;
       }
       while(transitions_A.hasNext() && transitions_B.hasNext() && equals){
            Triple<State,Character,State> current_A = transitions_A.next();
            Triple<State,Character,State> current_B = transitions_B.next();
            if(!(current_A.equals(current_B) && current_A.second().equals(current_B.second()))){
                equals = false;
            }
       }
       
       //Los mismos estados finales
       Set<State> st_final_A = new HashSet<>();
       st_final_A.addAll(A.estados_finales);
       Iterator<State> state_final_A = st_final_A.iterator();
       
       Set<State> st_final_B = new HashSet<>();
       st_final_B.addAll(B.estados_finales);
       Iterator<State> state_final_B = st_final_B.iterator();
       
       while(state_final_A.hasNext() && state_final_B.hasNext() && equals){
           State current_A = state_final_A.next();
           State current_B = state_final_B.next();
           if(!current_A.equals(current_B)){
               equals = false;
           }
       }
       
       //El mismo estado inicial
       
       if(A.inicial.equals(B.inicial)){
           equals = false;
       }
       
       return equals;
    }

   /** getTransiciones(): Retorna el conjunto de tranciciones
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @returns transiciones
    */
   public Set<Triple<State, Character, State>> getTransiciones() {
	   return transiciones;
   }
   
   
   /** grep(): Lee la entrada estándar o una lista de archivos e 
    * imprime las líneas que contengan coincidencias para la expresión regular. 
     * <li>pre: true </li>
     * <li>post:  </li>
     * <hr>
     * @param path_file 
     * @param regular_exp 
     * @returns String
    */
    public String grep(String regular_exp, String path_file) throws FileNotFoundException, IOException{
        assert rep_ok();
        assert (regular_exp != null);
        assert (path_file != null);
        
        //Con la expresion regular construyo el automata
        ExpresionRegular er = new ExpresionRegular(regular_exp);
        DFA automata = er.toDFA();
        
        String result = "";
        try{
            File file = new File (path_file);
            FileReader fr = new FileReader (file);
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
        
            String line = br.readLine();
            LinkedList<String> sub_String = new LinkedList<String>();
            while(line != null){
                sub_String = sub_List(line);
                for(int i=0; i<sub_String.size(); i++){
                	String text = sub_String.get(i);
                    if(automata.accepts(text)){
                        result += line + "/n";
                        break;
                    }
                }
                line = br.readLine();
            }
        }catch(FileNotFoundException ex){
            throw ex;
        }
        catch(IOException ex){
            throw ex;
        }
        return result;
    }
    
    
    /** sub_List(): Funcion que retorna una lista de string con todas los 
     * subString ordenado de un String
     * <li>pre: true </li>
     * <li>post: Una lista que contiene sub listas </li>
     * <hr>
     * @param text
     * @returns list
    */
    private LinkedList<String> sub_List(String text){
        LinkedList<String> list = new LinkedList();
        for(int i = 0; i < text.length(); i++){
            for(int j = i; j < text.length(); j++){
                list.add(text.substring(i, j+1));
            }
        }
        return list;
    }
    
}