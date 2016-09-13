package jug.junitassertj;

import org.junit.Assert;

public class CalculatorTest {
	@org.junit.Test
	public void shouldDivideByTwo() throws Exception {
		Calculator c = new Calculator();
		Assert.assertEquals(5, c.divide(10, 2));
	}

	@org.junit.Test
	public void shouldDivideBy2WithRounding() throws Exception {
		Calculator c = new Calculator();
		Assert.assertEquals(6, c.divide(13, 2));
	}
}
