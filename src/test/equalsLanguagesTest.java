/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import utils.Triple;
import automata.DFA;
import automata.FA;
import automata.State;

/**
 *
 * @author gaston
 */
public class equalsLanguagesTest {
    
    @Test
    public void test1() throws Exception {
        static DFA dfa_A = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        
        DFA dfa_B = (DFA) FA.parse_form_file("src/test/dfa2.dot");            
        
        assertTrue();
    }
    
}
