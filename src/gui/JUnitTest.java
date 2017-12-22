package gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class JUnitTest {

	@Test
	public void testRuleWeight() {
		Regra rule = new Regra("regra",0.0);
		assertEquals(0.0, rule.getWeight(), 0.0);
	}
	
	@Test
	public void testRuleName() {
		Regra rule = new Regra("regra",0.0);
		assertEquals("regra", rule.getName());
	}
	
}
