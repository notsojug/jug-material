package jug.junitassertj;

import org.junit.After;
import org.junit.Assert;

public class CalculatorBetterTest {
	
	Calculator cut; // Class Under Test
	
	@org.junit.Before
	public void setUp(){
		cut = new Calculator();
	}
	
	@org.junit.After
	public void tearDown(){
		System.out.println("Clean the mess before the next test(s)");
	}
	
	@org.junit.Test
	public void shouldDivideByTwo() throws Exception {
		Assert.assertEquals(5, cut.divide(10, 2));
	}

	@org.junit.Test
	public void shouldDivideBy2WithRounding() throws Exception {
		Calculator c = new Calculator();
		Assert.assertEquals(6, cut.divide(13, 2));
	}
}
