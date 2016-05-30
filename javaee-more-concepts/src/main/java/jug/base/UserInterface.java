package jug.base;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(typeAbstract="*Interface", typeImmutable="*", allParameters=true)
public interface UserInterface {
	int userId();
	
	String email();
	
	String passwordHash();
}
