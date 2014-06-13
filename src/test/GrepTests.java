package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import automata.DFA;
import automata.FA;

public class GrepTests {

	@Test
    public void test1() throws Exception {
        DFA dfa = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        String prueba = dfa.grep( "b.b", "src/test/prueba.txt");
        assertTrue(prueba.isEmpty());
    }
	
	@Test
    public void test2() throws Exception {
        DFA dfa = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        String prueba = dfa.grep( "i.w", "src/test/prueba.txt");
        String linea = "cardillo y kiwi"+ "/n";
        assertTrue(prueba.equals(linea));
    }
	
	@Test
    public void test3() throws Exception {
        DFA dfa = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        String prueba = dfa.grep( "((((f.e).l).i).z)", "src/test/prueba.txt");
        String linea = "hindu comia feliz "+ "/n";
        assertTrue(prueba.equals(linea));
    }
	
	@Test
    public void test4() throws Exception {
        DFA dfa = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        String prueba = dfa.grep( "a*", "src/test/prueba1.txt");
        assertTrue(!prueba.isEmpty());
    }
	
	@Test
    public void test5() throws Exception {
        DFA dfa = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        String prueba = dfa.grep( "b|c", "src/test/prueba1.txt");
        assertTrue(!prueba.isEmpty());
    }


}
