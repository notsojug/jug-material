package jug.guava;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Preconditions;

public class ParametersCheckingTest {
	
	private void aDangerousMethod(String aString, int someInt) {
		if (aString == null) {
			throw new NullPointerException("null string not permitted");
		}
		if (aString.isEmpty()) {
			throw new IllegalArgumentException("empty string not permitted");
		}

		if (someInt < 0 || someInt > 10) {
			throw new IllegalArgumentException("wrong int value");
		}
		// dosomething
	}
	
	
	@Test(expected=NullPointerException.class)
	public void shouldThrowNullPointerExceptionOnNullString() throws Exception {
		aDangerousMethod(null, 0);
		failBecauseExceptionWasNotThrown(NullPointerException.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowIlleglaArgumentOnEmptyString() throws Exception {
		aDangerousMethod("", 0);
		failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionOnBigInteger() throws Exception {
		aDangerousMethod("yo", 100);
		failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
	}
	
	
	private void aDangerousMethod_guava(String aString, int someInt){
		Preconditions.checkNotNull(aString, "string cannot be null");
		Preconditions.checkArgument(aString.isEmpty(), "String cannot be empty");
		Preconditions.checkArgument((someInt < 0 || someInt > 10), "wrong int value");
	}
	
	@Test(expected=NullPointerException.class)
	public void shouldThrowNullPointerExceptionOnNullString_guava() throws Exception {
		aDangerousMethod_guava(null, 0);
		failBecauseExceptionWasNotThrown(NullPointerException.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowIlleglaArgumentOnEmptyString_guava() throws Exception {
		aDangerousMethod_guava("", 0);
		failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionOnBigInteger_guava() throws Exception {
		aDangerousMethod_guava("yo", 100);
		failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
	}
}
