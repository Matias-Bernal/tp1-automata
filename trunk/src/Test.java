import automata.DFA;
import automata.FA;
import automata.NFALambda;

public class Test {

	public static void main(String[] args) {
		try {
			DFA dfaautomata = (DFA) FA.parse_form_file("src/test/dfa4.dot");
			System.out.println(dfaautomata.to_dot());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
