package jug.immutables;

import org.immutables.func.Functional;
import org.immutables.value.Value;

@Value.Immutable
// generates a class with no suffix or prefix
@Value.Style(typeAbstract="*Interface", typeImmutable="*")
@Functional
public interface MetadataInterface {
	@Value.Parameter
	String getName();

	@Value.Parameter
	String getSurname();

	@Value.Parameter
	int getAge();

	@Value.Parameter
	Role getRole();
}