package jug.srp.better3;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(typeAbstract="*Interface", typeImmutable="*")
public interface MailMessageInterface {
	@Value.Parameter
	String from();
	@Value.Parameter
	String to();
	@Value.Parameter
	String subject();
	@Value.Parameter
	String body();
}
