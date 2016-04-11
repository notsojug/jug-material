package jug.guava;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Fail;
import org.junit.Test;

import com.google.common.base.Splitter;

public class SplitStringTest {
	
	List<String> splitWords(String words, String separator){
		String[] strings = words.split(separator);
		ArrayList<String> result = new ArrayList<String>();
		for(String s: strings){
			final String trim = s.trim();
			if(!trim.isEmpty()){
				result.add(trim);
			}
		}
		return result;
	}
	
	@Test
	public void shouldSplitTwoWords() throws Exception {
		assertThat(splitWords("pippo,pluto", ","))
			.containsOnly("pippo", "pluto");
	}
	
	@Test
	public void shouldSplitTwoWordsWithSpaces() throws Exception {
		assertThat(splitWords("  pippo,  pluto  ", ","))
			.containsOnly("pippo", "pluto");
	}
	
	@Test
	public void shouldSplitTwoWordsWithSpacesAndEmptyStrings() throws Exception {
		assertThat(splitWords("  pippo, ,,,, pluto  ,,", ","))
			.containsOnly("pippo", "pluto");
	}
	
	Map<String, String> splitToMap(String string, String separator, String keyValueSeparator) {
		List<String> kvs = splitWords(string, separator);
		HashMap<String, String> map = new HashMap<String, String>();
		for (String one : kvs) {
			List<String> keyValue = splitWords(one, keyValueSeparator);
			if (keyValue.size() == 2) {
				map.put(keyValue.get(0), keyValue.get(1));
			} else {
				throw new IllegalArgumentException();
			}
		}
		return map;
	}

	@Test
	public void shouldSplitToMap() throws Exception {
		Map<String, String> split = splitToMap("yo=mama, so=fat", ",", "=");
		assertThat(split).containsEntry("yo", "mama")
			.containsEntry("so", "fat");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldSplitToMap_invalid() throws Exception {
		splitToMap("yo=mama, so=fat=lol", ",", "=");
		Fail.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
	}
	
	@Test
	public void shouldSplitTwoWords_guava() throws Exception {
		Iterable<String> split = split_guava("pippo,pluto", ",");
		assertThat(split).containsOnly("pippo", "pluto");
	}
	
	@Test
	public void shouldSplitTwoWordsWithSpaces_guava() throws Exception {
		Iterable<String> split = split_guava("  pippo,  pluto  ", ",");
		assertThat(split).containsOnly("pippo", "pluto");
	}
	
	@Test
	public void shouldSplitTwoWordsWithSpacesAndEmptyStrings_guava() throws Exception {
		Iterable<String> split = split_guava("  pippo, ,,,, pluto  ,,", ",");
		assertThat(split).containsOnly("pippo", "pluto");
	}

	private List<String> split_guava(final String string, String separator) {
		return Splitter.on(separator)
				.trimResults()
				.omitEmptyStrings()
				.splitToList(string);
	}
	
	@Test
	public void shouldSplitToMap_guava() throws Exception {
		String string = "yo=mama, so=fat";
		Map<String, String> split = splitToMap_guava(string, ",", "=");
		assertThat(split).containsEntry("yo", "mama")
			.containsEntry("so", "fat");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldSplitToMap_invalid_guava() throws Exception {
		String string = "yo=mama, so=fat=lol";
		splitToMap_guava(string, ",", "=");
		Fail.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
	}

	private Map<String, String> splitToMap_guava(String string, String separator, String keyValueSeparator) {
		return Splitter.on(separator)
				.trimResults()
				.withKeyValueSeparator(keyValueSeparator)
				.split(string);
	}
}
