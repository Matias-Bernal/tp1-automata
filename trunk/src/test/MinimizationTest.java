/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import automata.DFA;
import automata.FA;

/**
 *
 * @author gaston
 */
public class MinimizationTest {
    
    @Test
    public void test1() throws Exception {
        DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        dfa_A = dfa_A.minimizeDFA();
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        dfa_B = dfa_B.minimizeDFA();
        assertTrue(dfa_A.equalsAutomat(dfa_B));
    }
    
    @Test
    public void test2() throws Exception {
        DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa4.dot");            
        dfa_A = dfa_A.minimizeDFA();
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        dfa_B = dfa_B.minimizeDFA();
        assertFalse(dfa_A.equalsAutomat(dfa_B));
    }
    
    @Test
    public void test3() throws Exception {
        DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa4.dot");            
        dfa_A = dfa_A.minimizeDFA();
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa4.dot");            
        assertTrue(dfa_A.accepts("y") == dfa_B.accepts("y"));
    }
    
    @Test
    public void test4() throws Exception {
        DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa4.dot");            
        dfa_A = dfa_A.minimizeDFA();
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa4.dot");            
        assertTrue(dfa_A.accepts("hola") == dfa_B.accepts("hola"));
    }
    
    @Test
    public void test5() throws Exception {
        DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa4.dot");            
        dfa_A = dfa_A.minimizeDFA();
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa4.dot");
        assertTrue(dfa_A.accepts("automatas") == dfa_B.accepts("automatas"));
    }   
    
    @Test
    public void test6() throws Exception {
        DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa3.dot");            
        dfa_A = dfa_A.minimizeDFA();
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa3.dot");
        assertTrue(dfa_A.accepts("bb") == dfa_B.accepts("bb"));
    }   
    
}
