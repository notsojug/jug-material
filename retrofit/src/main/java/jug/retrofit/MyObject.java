package jug.retrofit;

import com.google.common.base.Optional;

/**
 * A simple bean to be serialized.
 */
public class MyObject {
	private String firstField;
	private Integer secondField;

	public Optional<String> getFirstField() {
		return Optional.fromNullable(firstField);
	}

	public Optional<Integer> getSecondField() {
		return Optional.fromNullable(secondField);
	}
	
	public static MyObject create(String firstField, Integer secondField) {
		MyObject toreturn = new MyObject();
		toreturn.firstField = firstField;
		toreturn.secondField = secondField;
		return toreturn;
	}
}