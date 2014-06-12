package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utils.ParserDescendienteRecursivoLL1;

public class ParserDescendienteLL1Test {

	@Test
	public void test1() throws Exception {
		ParserDescendienteRecursivoLL1 my_parser = new ParserDescendienteRecursivoLL1("a");	
		assertTrue(my_parser.iniciarParser());
	}

	@Test
	public void test2() throws Exception {
		ParserDescendienteRecursivoLL1 my_parser = new ParserDescendienteRecursivoLL1("a.b");	
		assertTrue(my_parser.iniciarParser());
	}
	
	@Test
	public void test3() throws Exception {
		ParserDescendienteRecursivoLL1 my_parser = new ParserDescendienteRecursivoLL1("a|b");	
		assertTrue(my_parser.iniciarParser());
	}
	
	@Test
	public void test4() throws Exception {
		ParserDescendienteRecursivoLL1 my_parser = new ParserDescendienteRecursivoLL1("a*");	
		assertTrue(my_parser.iniciarParser());
	}
	
	@Test
	public void test5() throws Exception {
		ParserDescendienteRecursivoLL1 my_parser = new ParserDescendienteRecursivoLL1("(b.c)|a*");	
		assertTrue(my_parser.iniciarParser());
	}
}
