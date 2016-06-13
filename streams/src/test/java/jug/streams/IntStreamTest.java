package jug.streams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.assertj.core.data.Offset;
import org.junit.Test;

public class IntStreamTest {
	@Test
	public void shouldGetRightStatistics() throws Exception {
		IntSummaryStatistics statistics = IntStream.range(1, 101)
			.summaryStatistics();
		
		assertThat(statistics.getSum()).isEqualTo(5050L);
		assertThat(statistics.getMax()).isEqualTo(100);
		assertThat(statistics.getMin()).isEqualTo(1);
		assertThat(statistics.getAverage()).isEqualTo(50.50, Offset.offset(0.05d));
	}
	
	@Test
	public void shouldGenerateWithIterator() throws Exception {
		// iteratively build infinite stream
		String result = IntStream.iterate(5, x -> x + 5)
			.limit(5) // limit size
			.boxed()  // turn to Integer
			.map(Object::toString) // convert to string
			.collect(Collectors.joining(",")); // join
		
		assertThat(result).isEqualTo("5,10,15,20,25");
	}
}

