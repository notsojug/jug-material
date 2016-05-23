package jug.inject3;

import javax.enterprise.inject.Produces;

public class VariousConstructorComplexFactory {
//	@Produces
//	public VariousConstructors createVariousConstructor() {
//		System.out.println("I've been called");
//		return new VariousConstructors("factory");
//	}

	@Produces
	@Configured
	public VariousConstructors createVariousConstructor_configured() {
		System.out.println("I've been called - configured");
		return new VariousConstructors("factory and configured");
	}
}
