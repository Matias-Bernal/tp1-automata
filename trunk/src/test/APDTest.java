package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import automata.APD;

public class APDTest {

	@Test
	public void creation_test() throws Exception {
		APD my_apd = new APD("src/test/apd1.dot");	
		assertTrue(my_apd.rep_ok());
	}
	
	@Test
	public void accept_test1() throws Exception {
		APD my_apd = new APD("src/test/apd1.dot");	
		assertFalse(my_apd.accept_by_final_state("a"));
	}
	
	@Test
	public void accept_test2() throws Exception {
		APD my_apd = new APD("src/test/apd1.dot");	
		assertFalse(my_apd.accept_by_final_state("aa"));
	}
	
	@Test
	public void accept_test3() throws Exception {
		APD my_apd = new APD("src/test/apd1.dot");	
		assertTrue(my_apd.accept_by_final_state("aaab"));
	}
	
	@Test
	public void accept_test4() throws Exception {
		APD my_apd = new APD("src/test/apd1.dot");	
		assertTrue(my_apd.accept_by_final_state("ab"));
	}
	

}
