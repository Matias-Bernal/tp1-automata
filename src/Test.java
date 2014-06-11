import utils.ExpresionRegular;

public class Test {

	public static void main(String[] args) {
		try {
			String cadena = "a.b";
			ExpresionRegular er = new ExpresionRegular(cadena);
			System.out.println(er.toDFA().to_dot());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}