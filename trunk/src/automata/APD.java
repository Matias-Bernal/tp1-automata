package automata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Quintuple;
import utils.TadPilaDinPila;

public class APD {

	/*	
	 * 	Construction
	*/
	
	// Constructor
    Set<State> estados;		
    Set<Character> alfabeto;
    Set<Character> alfabeto_pila;
    Set<Quintuple<State,Character,String,Character,State>> transiciones;		
    State estado_inicial;
    Set<State> estados_finales;
    TadPilaDinPila pila;
    
	public static final Character Lambda = '_';
	public static final Character Simbolo_Inicial = '@';

	@SuppressWarnings("resource")
	public APD(String path_file) throws IllegalArgumentException, IOException {	
		//Abrir el archivo
		File file = new File (path_file);;
		FileReader fr = new FileReader (file);
		BufferedReader br = new BufferedReader(fr);
		
		estados = new HashSet<State>();
		alfabeto = new HashSet<Character>();
		alfabeto_pila = new HashSet<Character>();
		transiciones = new HashSet<Quintuple<State,Character,String,Character,State>>();
		estados_finales = new HashSet<State>();
		pila = new TadPilaDinPila();
		pila.apilar(Simbolo_Inicial);
		
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
			throw new IllegalArgumentException();
		//primera linea//
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
				throw new IllegalArgumentException();
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
					throw new IllegalArgumentException();
				//////////////////////////////////////////////////
				pat = Pattern.compile("^(\\n|\t|\\s)*inic->[a-z�A-Z�]+\\d+;$");
				mat = pat.matcher(line);
				if (mat.matches()) {
					//Guardar el estado Inicial
					estado_inicial = new State(line.substring(line.indexOf('q'), line.indexOf(';'))); //cambiar q por [a-z�A-Z�]+\\d+
					if(!estados.contains(estado_inicial))
						estados.add(estado_inicial);
					//Guardar los estados y las transiciones
					line = br.readLine();
					/////////Eliminar si hay lineas en blanco//////////
					matBlanco = patBlanco.matcher(line);
					while (matBlanco.matches() && line!=null){
						line = br.readLine();
						matBlanco = patBlanco.matcher(line);
					}
					if(line==null)
						throw new IllegalArgumentException();
					//////////////////////////////////////////////////					
					Pattern pat1 = Pattern.compile("^(\\n|\t|\\s)*[a-z�A-Z�]+\\d+\\[shape=doublecircle\\];$");
					Matcher mat1 = pat1.matcher(line);
					Pattern pat2 = Pattern.compile("^(\\n|\t|\\s)*[a-z�A-Z�]+\\d+->[a-z�A-Z�]+\\d+\\s\\[label=\".+\"\\];$");
					Matcher mat2 = pat2.matcher(line);
					while(!mat1.matches() && mat2.matches()){ //no son el/los estados finales
						State from = new State(line.substring(line.indexOf('q'), line.indexOf('-')));
						State to = new State(line.substring(line.indexOf(">")+1, line.indexOf('[')-1));
						if(!estados.contains(from))
							estados.add(from);
						if(!estados.contains(to))
							estados.add(to);
						String labels = line.substring(line.indexOf('"')+1, line.indexOf(']')-1);
						if(labels.length()>4){
							labels.replaceAll("\b", "");
							String[] labelsStr = labels.split("/");
							if (labelsStr.length ==3){
								Character label = labelsStr[0].charAt(0);
								if(!alfabeto.contains(label))
									alfabeto.add(label);
								Character label_pila = labelsStr[1].charAt(0);
								if(!alfabeto_pila.contains(label_pila))
									alfabeto_pila.add(label_pila);
								String simbolo_pila = labelsStr[2].toString();
								Quintuple<State,Character,String,Character,State> transition = new Quintuple<State,Character,String,Character,State>(from, label, simbolo_pila, label_pila, to);
								if(!transiciones.contains(transition))
									transiciones.add(transition);	
							}else{
								throw new IllegalArgumentException();
							}
						}else{
							throw new IllegalArgumentException();
						}
						line = br.readLine();
						/////////Eliminar si hay lineas en blanco//////////
						matBlanco = patBlanco.matcher(line);
						while (matBlanco.matches() && line!=null){
							line = br.readLine();
							matBlanco = patBlanco.matcher(line);
						}
						if(line==null)
							throw new IllegalArgumentException();
						//////////////////////////////////////////////////
						mat1 = pat1.matcher(line);
						mat2 = pat2.matcher(line);
					}
					//line=br.readLine(); //linea en blanco antes de los estados finales
					//Guarda el primer de el/los estado/s final/es
					State final_state = new State(line.substring(line.indexOf('q'), line.indexOf('[')));
					if(!estados.contains(final_state))
						estados.add(final_state);
					estados_finales.add(final_state);
						
					line=br.readLine();
					/////////Eliminar si hay lineas en blanco//////////
					matBlanco = patBlanco.matcher(line);
					while (matBlanco.matches() && line!=null){
						line = br.readLine();
						matBlanco = patBlanco.matcher(line);
					}
					if(line==null)
						throw new IllegalArgumentException();
					//////////////////////////////////////////////////
					pat = Pattern.compile("^(\\n|\t|\\s*)[a-z�A-Z�]+\\d+\\[shape=doublecircle\\];$");
					while(mat.matches() || line!=null){ //Guardar el/los estados finales
						mat = pat.matcher(line);
						if(mat.matches()){ //Es estado final
							final_state = new State(line.substring(line.indexOf('q'), line.indexOf('[')));
							if(!estados.contains(final_state))
								estados.add(final_state);
							estados_finales.add(final_state);
							line=br.readLine();
							/////////Eliminar si hay lineas en blanco//////////
							matBlanco = patBlanco.matcher(line);
							while (matBlanco.matches() && line!=null){
								line = br.readLine();
								matBlanco = patBlanco.matcher(line);
							}
							if(line==null)
								throw new IllegalArgumentException();
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

						}else{
							throw new IllegalArgumentException();
						}									
					}else{
						throw new IllegalArgumentException();
					}
				}
			}else{
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}
		if(!rep_ok()){
			throw new IllegalArgumentException();		
		}
	}
	/*
	 *	State querying 
	 */

	
	
	/**
	 * @return True iff the automaton is in a consistent state.
	 */
	public boolean rep_ok() {
		// TODO Auto-generated method stub
		return true;
	}
	
}