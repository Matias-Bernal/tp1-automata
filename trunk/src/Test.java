import automata.APD;

public class Test {

	public static void main(String[] args) {
		try {
			APD apd = new APD("src/test/apd1.dot");
			apd.rep_ok();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}