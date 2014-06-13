/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import automata.DFA;
import automata.FA;
import static org.junit.Assert.*;
import org.junit.Test;

public class EqualsLenguageTest {
    
    @Test
    public void test1() throws Exception {
        DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa2.dot");
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa2.dot");
        assertTrue(dfa_A.equalsLenguage(dfa_B));
    }
    
    @Test
    public void test2() throws Exception {
        DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa2.dot");
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa4.dot");
        assertFalse(dfa_A.equalsLenguage(dfa_B));
    }
    
    
    @Test
    public void test3() throws Exception {
        DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa2.dot");
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa3.dot");
        assertFalse(dfa_A.equalsLenguage(dfa_B));
    }
}
