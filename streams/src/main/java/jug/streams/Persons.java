package jug.streams;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Splitter;

public class Persons {
	private static final Splitter SPLITTER = Splitter.on(',').trimResults();

	public static Person parse(String s){
		List<String> elements = SPLITTER.splitToList(s);
		return ImmutablePerson.builder()
				.name(elements.get(0))
				.age(tryParseInt(elements.get(1)).orElse(20))
				.yearsOfExperience(tryParseInt(elements.get(2)).orElse(0))
				.menthorName(elements.size() > 3 ? elements.get(3) : null)
				.build();
	}
	
	
	private static Optional<Integer> tryParseInt(String s){
		try {
			return Optional.of(Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
}

