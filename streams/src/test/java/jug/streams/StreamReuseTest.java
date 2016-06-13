package jug.streams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;

public class StreamReuseTest {

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionOnReuse() throws Exception {
		// save a partially defined pipeline as a variable
		Stream<Integer> stream = Stream.of(1, 5, 7, 8, 10).filter(x -> x % 2 == 0);

		// perform a terminal operation
		Optional<Integer> first = stream.findFirst();
		assertThat(first).isPresent();

		// try to reuse a stream
		stream.count();

		// should not reach this line (hint: it does not)
		failBecauseExceptionWasNotThrown(IllegalStateException.class);
	}

}
