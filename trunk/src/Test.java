import automata.APD;

public class Test {

	public static void main(String[] args) {
		try {
			APD apd = new APD("src/test/apd1.dot");
			System.out.println(apd.accept_by_final_state("ab"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}