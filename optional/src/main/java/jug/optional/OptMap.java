package jug.optional;

import java.util.HashMap;
import java.util.Optional;

/**
 * Good ol map Optional powered!
 */
public class OptMap<T, U> extends HashMap<T, U> {
	private static final long serialVersionUID = -762490387382850064L;

	public Optional<U> find(T key) {
		return Optional.ofNullable(super.get(key));
	}
}
