package jug.guava;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.Splitter;

public class SplitterTest {
	@Test
	public void shouldSplitTwoWords() throws Exception {
		Iterable<String> split = Splitter.on(' ').split("pippo pluto");
		assertThat(split).containsOnly("pippo", "pluto");
	}
	
	@Test
	public void shouldSplitTwoWordsWithSpaces() throws Exception {
		Iterable<String> split = Splitter.on(',').trimResults().split("  pippo,  pluto  ");
		assertThat(split).containsOnly("pippo", "pluto");
	}
	
	@Test
	public void shouldSplitTwoWordsWithSpacesAndEmptyStrings() throws Exception {
		Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split("  pippo, ,,,, pluto  ,,");
		assertThat(split).containsOnly("pippo", "pluto");
	}
	
	@Test
	public void shouldSplitToMap() throws Exception {
		String string = "yo=mama; so=fat";
		Map<String, String> split = Splitter.on(";")
				.trimResults()
				.withKeyValueSeparator("=")
				.split(string);
		assertThat(split).containsEntry("yo", "mama")
			.containsEntry("so", "fat");
	}
}
