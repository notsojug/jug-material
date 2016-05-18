package jug.inject2;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(typeAbstract="Abstract*", typeImmutable="*", allParameters=true)
public interface AbstractImmutableWithFactory {
	String getId();
}
