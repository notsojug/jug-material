package jug.base;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(typeAbstract="*Interface", typeImmutable="*")
public interface UserInterface {
	@Value.Parameter
	String userId();
}
