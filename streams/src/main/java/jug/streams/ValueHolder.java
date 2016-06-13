package jug.streams;

import org.immutables.value.Value;

import com.google.common.collect.ImmutableList;

/**
 * A generic interface to demonstrate the flatmap facility
 *
 */
@Value.Immutable
@Value.Style(allParameters = true)
public interface ValueHolder {
	ImmutableList<String> getList();

	static ValueHolder of(Iterable<String> strings) {
		return ImmutableValueHolder.of(strings);
	}

	static ValueHolder empty() {
		return ImmutableValueHolder.of(ImmutableList.of());
	}
}
