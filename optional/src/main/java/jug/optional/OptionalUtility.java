package jug.optional;

import java.util.Optional;

public class OptionalUtility {
	
	/**
	 *  Avoid OptionalInt, it lacks map and flatMap methods!!
	 */
	public static Optional<Integer> stringToInt(String s) {
		try {
			return Optional.of(Integer.valueOf(s));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
	
}
