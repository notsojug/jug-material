package jug.streams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class StreamsOfOptionalsTest {
	
	static Optional<String> stringOver5Chars(String original){
		if(original.length()>5)
			return Optional.<String> of(original);
		else
			return Optional.<String> empty();
	}
	
	@Test
	public void wrongPipelineShouldLeadToEmptyList() throws Exception {
		Stream<Optional<String>> partial = Stream.iterate("", x -> x + "X")
			.map(x -> stringOver5Chars(x));
		
		// booo... we have a list of optionals! what a waste of objects
		List<String> presentStrings = partial
			.filter(Optional::isPresent) // keep those which are present
			.map(Optional::get) // get the values
			.limit(2)
			.collect(Collectors.toList());
		
		assertThat(presentStrings).contains("XXXXXX", "XXXXXXX");
	}
	
	/**
	 * Flatten an optional to either a stream of the content, or an empty stream
	 */
	static <T> Stream<T> flatten(Optional<T> optional){
		return optional.map(Stream::of)
				.orElse(Stream.empty());
	}
	
	@Test
	public void flatMap_shouldLeadToListOfTwo() throws Exception {
		List<String> strings = Stream.iterate("", x -> x + "X")
				.map(x -> stringOver5Chars(x))
				.flatMap(opt -> flatten(opt))
				.limit(2)
				.collect(Collectors.toList());
		
		assertThat(strings).contains("XXXXXX", "XXXXXXX");
	}
}
