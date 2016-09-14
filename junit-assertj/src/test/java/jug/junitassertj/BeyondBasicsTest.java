package jug.junitassertj;

import static org.junit.Assert.*;

import org.junit.Test;

public class BeyondBasicsTest {
	@Test(expected = ArithmeticException.class)
	public void dividingByZeroShouldThrowArithmeticException() throws Exception {
		int a = 5 / 0;
	}
}
