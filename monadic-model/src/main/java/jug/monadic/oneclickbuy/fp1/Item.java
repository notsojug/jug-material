package jug.monadic.oneclickbuy.fp1;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allParameters = true)
public interface Item {
	String name();

	static Item of(String name) {
		return ImmutableItem.of(name);
	}
}
