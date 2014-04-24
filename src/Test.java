import automata.DFA;
import automata.FA;
import automata.NFALambda;

public class Test {

	public static void main(String[] args) {
		try {
			DFA dfaautomata = (DFA) FA.parse_form_file("src/test/dfa1.dot");
			System.out.println(dfaautomata.to_dot());
			NFALambda nfaLambdaAutomata = dfaautomata.toNFALambda();
			System.out.println(nfaLambdaAutomata.to_dot());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
