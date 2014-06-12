package test;

import static org.junit.Assert.*;

import org.junit.Test;

import automata.DFA;
import automata.FA;
import utils.ExpresionRegular;
import utils.ParserDescendienteRecursivoLL1;

public class ExpresionRegularTest {

	@Test
	public void test1() throws Exception {
		ExpresionRegular my_er = new ExpresionRegular("a");
		FA dfa1 = my_er.toDFA();		
		assertTrue(dfa1 instanceof DFA);
	}

	@Test
	public void test2() throws Exception {
		ExpresionRegular my_er = new ExpresionRegular("a.b");	
		FA dfa1 = my_er.toDFA();		
		assertTrue(dfa1 instanceof DFA);
	}
	
	@Test
	public void test3() throws Exception {
		ExpresionRegular my_er = new ExpresionRegular("a|b");	
		FA dfa1 = my_er.toDFA();		
		assertTrue(dfa1 instanceof DFA);
	}
	
	@Test
	public void test4() throws Exception {
		ExpresionRegular my_er = new ExpresionRegular("a*");	
		FA dfa1 = my_er.toDFA();		
		assertTrue(dfa1 instanceof DFA);
	}
	
	@Test
	public void test5() throws Exception {
		ExpresionRegular my_er = new ExpresionRegular("(b.c)|a*");	
		FA dfa1 = my_er.toDFA();		
		assertTrue(dfa1 instanceof DFA);
	}
	
	@Test
	public void test6() throws Exception {
		ExpresionRegular my_er = new ExpresionRegular("(b.c)|a*");	
		FA dfa1 = my_er.toDFA();		
		assertTrue(dfa1.accepts("aaaaaa"));
	}
	
	@Test
	public void test7() throws Exception {
		ExpresionRegular my_er = new ExpresionRegular("(b.c)|a*");	
		FA dfa1 = my_er.toDFA();		
		assertFalse(dfa1.accepts("b"));
	}

}
