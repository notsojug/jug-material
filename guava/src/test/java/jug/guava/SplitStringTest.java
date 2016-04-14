package jug.guava;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Fail;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;

/**
 * A test to explore the {@link Splitter} class from guava. 
 *
 */
public class SplitStringTest {
	
	/**
	 * A method to split words from a string, giving the separator. The method
	 * tolerates empty values, removing them from the result. The method also
	 * trims the resulting words.
	 * 
	 * @param words
	 *            the string to split
	 * @param separator
	 *            the separator for the words.
	 * @return a list of splitted strings.
	 */
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
	
	/*
	 * from here on, you can find a series of test to assert the behavior of the
	 * function
	 */
	
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
	
	/*
	 * Now let's try something more complex: from a string of key-value list, create a map.
	 * <p>
	 * e.g. <code> hello=it, is=me </code> should get a map with 2 keys and 2 values.
	 */
	
	/**
	 * A method to get a map from a string consisting in key-value pairs
	 * separated by two kinds of separators: one for the key and value, one for
	 * the pairs.
	 * 
	 * @param string
	 *            the string to split.
	 * @param separator
	 *            the separator of the key-value pairs.
	 * @param keyValueSeparator
	 *            the separator between key and value.
	 * @return a map from the given string.
	 */
	Map<String, String> splitToMap(String string, String separator, String keyValueSeparator) {
		List<String> kvs = splitWords(string, separator);
		HashMap<String, String> map = new HashMap<String, String>();
		for (String one : kvs) {
			List<String> keyValue = splitWords(one, keyValueSeparator);
			if (keyValue.size() == 2) {
				map.put(keyValue.get(0), keyValue.get(1));
			} else {
				throw new IllegalArgumentException("Splitting the key and the vlue didn't result in a pair of values");
			}
		}
		return map;
	}

	/*
	 * from here on, you can find a series of test to assert the behavior of the
	 * function
	 */
	
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
	
	
	/*
	 * Now let's try to do the same as above, but with guava.
	 */

	/**
	 * A method to split words from a string, giving the separator. The method
	 * tolerates empty values, removing them from the result. The method also
	 * trims the resulting words.
	 * <p>
	 * This method uses guava's {@link Splitter} to achieve the result.
	 * 
	 * @param words
	 *            the string to split
	 * @param separator
	 *            the separator for the words.
	 * @return a list of splitted strings.
	 */
	private List<String> split_guava(final String string, String separator) {
		return Splitter.on(separator)	// split using the separator
				.trimResults()			// trim each resulting splitted word
				.omitEmptyStrings()		// omit results which are empty
				.splitToList(string);	// get a list instead of an iterable 
		
		/*
		 * each of these calls produces a new, different Splitter, which you can
		 * cache for later use, since they are immutable and thread safe
		 */
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
	
	/**
	 * A method to get a map from a string consisting in key-value pairs
	 * separated by two kinds of separators: one for the key and value, one for
	 * the pairs.
	 * <p>
	 * This method uses guava's {@link Splitter} and {@link MapSplitter} to
	 * achieve the result.
	 * 
	 * @param string
	 *            the string to split.
	 * @param separator
	 *            the separator of the key-value pairs.
	 * @param keyValueSeparator
	 *            the separator between key and value.
	 * @return a map from the given string.
	 */
	private Map<String, String> splitToMap_guava(String string, String separator, String keyValueSeparator) {
		return Splitter.on(separator)	// split using the separator
				.trimResults()			// trim each resulting splitted word
				// use the second separator as a key-value separator
				.withKeyValueSeparator(keyValueSeparator) 
				.split(string);			// split to a map
	}
	
	/*
	 * test the behavior of the above function
	 */
	
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

}
