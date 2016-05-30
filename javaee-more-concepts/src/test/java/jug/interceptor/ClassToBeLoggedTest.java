package jug.interceptor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ClassToBeLoggedTest {
	
	ClassToBeLogged cut=new ClassToBeLogged(); // class under test;
	
	@Test
	public void shouldOutputString() throws Exception {
		assertThat(cut.someMethod("high", 5)).isEqualTo("been there, done that");
	}
	
}
