package jug.streams;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;

public class FileReadingTest {
	@Test
	public void shouldReadAllMenthorAndExtractOne() throws Exception {
		URI fileName = getClass().getResource("test.csv").toURI();
		
		/*
		 * Get all the menthors from people below 30, skip one, get one, print
		 * its name, collecto to variable.
		 */
		
		try(Stream<String> stream=Files.lines(Paths.get(fileName))){
			Optional<String> findFirst = stream.map(Persons::parse)
					.filter(p -> p.age() < 30)
					.filter(p -> p.menthorName().isPresent())
					.map(Person::menthorName)
					.map(Optional::get)
					.skip(1)
					.limit(1)
					.peek(System.out::println)
					.findFirst();
			
			assertThat(findFirst)
				.isNotEmpty()
				.contains("Mario Fusco");
		} catch (IOException e) {
			System.out.println("An error occoured");
		} 
	}
}
