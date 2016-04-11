package jug.guava;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;

import org.assertj.core.api.Fail;
import org.junit.Test;

import com.google.common.base.Throwables;

public class ExceptionHandlingTest {

	<T extends Exception> void callUglyCode(ExceptionThrower<T> et) throws IOException {
		try {
			et.throwSomething();
		} catch (Exception e) {
			if (e instanceof IOException) {
				throw IOException.class.cast(e);
			}
			if (RuntimeException.class.isAssignableFrom(e.getClass())) {
				throw RuntimeException.class.cast(e);
			}
			throw new RuntimeException(e);
		}

	}

	@Test(expected=IOException.class)
	public void shouldRethrowIOException() throws Exception {
		ExceptionThrower<Exception> et = new ExceptionThrower<Exception>(new IOException());
		callUglyCode(et);
		Fail.failBecauseExceptionWasNotThrown(IOException.class);
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldWrapSQLException() throws Exception {
		ExceptionThrower<Exception> et = new ExceptionThrower<Exception>(new SQLException());
		callUglyCode(et);
		Fail.failBecauseExceptionWasNotThrown(RuntimeException.class);
	}
	
	@Test
	public void shouldNotWrapRuntimeException() throws Exception {
		final RuntimeException someException = new RuntimeException();
		ExceptionThrower<Exception> et = new ExceptionThrower<Exception>(someException);
		try {
			callUglyCode(et);
			Fail.failBecauseExceptionWasNotThrown(RuntimeException.class);
		} catch (RuntimeException e) {
			assertThat(e).isSameAs(someException);
		}
	}

	<T extends Exception> void callUglyCode_guava(ExceptionThrower<T> et) throws IOException {
		try {
			et.throwSomething();
		} catch (Exception e) {
			Throwables.propagateIfInstanceOf(e, IOException.class);
			throw Throwables.propagate(e);
		}
	}
	
	@Test(expected=IOException.class)
	public void shouldRethrowIOException_guava() throws Exception {
		ExceptionThrower<Exception> et = new ExceptionThrower<Exception>(new IOException());
		callUglyCode_guava(et);
		Fail.failBecauseExceptionWasNotThrown(IOException.class);
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldWrapSQLException_guava() throws Exception {
		ExceptionThrower<Exception> et = new ExceptionThrower<Exception>(new SQLException());
		callUglyCode_guava(et);
		Fail.failBecauseExceptionWasNotThrown(RuntimeException.class);
	}
	
	@Test
	public void shouldNotWrapRuntimeException_guava() throws Exception {
		final RuntimeException someException = new RuntimeException();
		ExceptionThrower<Exception> et = new ExceptionThrower<Exception>(someException);
		try {
			callUglyCode_guava(et);
			Fail.failBecauseExceptionWasNotThrown(RuntimeException.class);
		} catch (RuntimeException e) {
			assertThat(e).isSameAs(someException);
		}
	}

	private class ExceptionThrower<T extends Exception> {
		T ex;

		private ExceptionThrower(T ex) {
			super();
			this.ex = ex;
		}

		public void throwSomething() throws Exception {
			throw ex;
		}
	}
}
