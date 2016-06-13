package jug.streams;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allParameters=true, optionalAcceptNullable=true)
public interface Person {
	String name();
	
	int age();
	
	int yearsOfExperience();
	
	Optional<String> menthorName();
}
