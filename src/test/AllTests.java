package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ DFAAutomataMethodsTests.class, DFACreationTests.class,
		DFAStateQueryingTests.class, IntegrationTests.class,
		NFAAutomataMethodsTests.class, NFACreationTests.class,
		NFALambdaAutomataMethodsTests.class, NFALambdaCreationTests.class,
		NFALambdaStateQueryingTests.class, NFAStateQueryingTests.class, APDTest.class, GrepTests.class, ParserDescendienteLL1Test.class, ExpresionRegularTest.class })
public class AllTests {

}
