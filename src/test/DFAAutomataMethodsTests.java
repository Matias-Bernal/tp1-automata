package test;

import static org.junit.Assert.*;

import org.junit.Test;

import automata.DFA;
import automata.FA;


public class DFAAutomataMethodsTests { 

	// Tests for DFA1
	
	@Test
	public void test1() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa1.dot");
		assertTrue(dfa.accepts("abbb"));
	}

	@Test
	public void test2() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa1.dot");
		assertFalse(dfa.accepts("ababb"));
	}
	
	@Test
	public void test3() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa1.dot");
		assertFalse(dfa.complement().accepts("abbb"));
	}
	
	@Test
	public void test4() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa1.dot");
		assertTrue(dfa.complement().accepts("ababb"));
	}
	
	// Tests for DFA2
	
	@Test
	public void test5() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa2.dot");
		assertTrue(dfa.accepts("bbbbbb"));
	}

	@Test
	public void test6() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa2.dot");
		assertFalse(dfa.accepts("bbbbb"));
	}
	
	@Test
	public void test7() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa2.dot");
		assertFalse(dfa.complement().accepts("bb"));
	}
	
	@Test
	public void test8() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa2.dot");
		assertTrue(dfa.complement().accepts("b"));
	}
	
	// Tests for DFA3
	
	@Test
	public void test9() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa3.dot");
		assertTrue(dfa.accepts("aaa"));
	}

	@Test
	public void test10() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa3.dot");
		assertFalse(dfa.accepts("aa"));
	}
	
	@Test
	public void test11() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa3.dot");
		assertFalse(dfa.complement().accepts("aaa"));
	}
	
	@Test
	public void test12() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa3.dot");
		assertTrue(dfa.complement().accepts("aa"));
	}	
	
	// Other Tests
	
	@Test
	public void test13() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa3.dot");
		assertFalse(dfa.is_empty());
	}
	
	@Test
	public void test14() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa3.dot");
		assertTrue(dfa.intersection(dfa.complement()).is_empty());
	}
	
	@Test
	public void test15() throws Exception {
		DFA dfa2 = (DFA) FA.parse_form_file("src/test/dfa2.dot");
		DFA dfa3 = (DFA) FA.parse_form_file("src/test/dfa3.dot");
		DFA union = dfa2.union(dfa3);
		assertTrue(union.accepts("a") && union.accepts("bbbb"));
	}	
	
	@Test
	public void test16() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa3.dot");
		assertFalse(dfa.is_finite());
	}
	
	@Test
	public void test17() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa4.dot");
		assertTrue(dfa.is_finite());
	}
	
	@Test
	public void test18() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa4.dot");
		assertTrue(dfa.star().accepts("automatasyautomatasyautomatasyautomatasyautomatasyautomatas"));
	}
	
	@Test
	public void test19() throws Exception {
		DFA dfa = (DFA) FA.parse_form_file("src/test/dfa4.dot");
		assertFalse(dfa.star().is_finite());
	}
	
}
