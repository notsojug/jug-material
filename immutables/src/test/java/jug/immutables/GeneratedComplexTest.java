package jug.immutables;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import jug.immutables.AbstractComplex;
import jug.immutables.GeneratedComplex;

public class GeneratedComplexTest {
	@Test
	public void shouldCorrectlySubtractToONE() throws Exception {
		AbstractComplex oneAndOne = GeneratedComplex.of(1, 1);
		AbstractComplex shouldBeOne = oneAndOne.subtract(AbstractComplex.I);
		assertThat(shouldBeOne).isEqualTo(AbstractComplex.ONE);
	}
	
	@Test
	public void withMethodsShouldGenerateDifferentInstances() throws Exception {
		GeneratedComplex oneAndOne = GeneratedComplex.of(1, 1);
		GeneratedComplex shouldEqualToI = oneAndOne.withRe(0);
		assertThat(shouldEqualToI)
			// different object from the starting one
			.isNotSameAs(oneAndOne)
			// not I from AbstractComplex
			.isNotSameAs(AbstractComplex.I)
			// still equal to I from AbstractComplex
			.isEqualTo(AbstractComplex.I);
	}
	
	@Test
	public void withMethodsShouldGenerateDifferentInstances_2() throws Exception {
		GeneratedComplex oneAndOne = GeneratedComplex.of(1, 1);
		GeneratedComplex shouldBeNotSameButEqual = oneAndOne.withRe(0).withRe(1);
		assertThat(shouldBeNotSameButEqual)
			// different object from the starting one
			.isNotSameAs(oneAndOne)
			// qual to the starting one
			.isEqualTo(oneAndOne);
	}
}
