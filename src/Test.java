import automata.DFA;
import utils.ExpresionRegular;

public class Test {

	public static void main(String[] args) {
		try {
			String cadena = "(a.b)|((c*).(d*))";
			ExpresionRegular er = new ExpresionRegular(cadena);
			DFA aux = er.toDFA();
			System.out.println(aux.to_dot());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}