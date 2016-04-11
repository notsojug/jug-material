package jug.guava;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class FileProcessingTest {
	private static final String ERROR_PREFIX = "[ERROR]";

	List<String> findFirst10Errors(File file) throws Exception {
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		final ArrayList<String> output = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			if (line.startsWith(ERROR_PREFIX)) {
				output.add(line);
			}
			if (output.size() >= 10) {
				break;
			}
		}

		reader.close();
		return output;
	}
	
	@Test
	public void shouldRead10Errors() throws Exception {
		File file = new File(getClass().getResource("test.log").toURI());
		assertThat(findFirst10Errors(file))
			.hasSize(10)
			.are(startingWithError());
	}
	
	private List<String> findFirst10Errors_guava(File file) throws IOException {
		List<String> lines = Files.readLines(file, Charsets.UTF_8, new LineProcessor<List<String>>() {
			List<String> accumulator = new ArrayList<String>();
			
			@Override
			public boolean processLine(String line) throws IOException {
				if (line.startsWith(ERROR_PREFIX)) {
					accumulator.add(line);
				}
				return accumulator.size() < 10;
			}

			@Override
			public List<String> getResult() {
				return accumulator;
			}
		});
		return lines;
	}
	
	@Test
	public void shouldRead10Errors_guava() throws Exception {
		File file = new File(getClass().getResource("test.log").toURI());
		assertThat(findFirst10Errors_guava(file))
			.hasSize(10)
			.are(startingWithError());
	}

	private static Condition<String> startingWithError() {
		return new Condition<String>() {
			@Override
			public boolean matches(String value) {
				return value.startsWith(ERROR_PREFIX);
			}
		};
	}

	
}
