package jug.optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.guava.api.Assertions.*;

import java.io.Serializable;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class OptionalGuavaTest {

	Map<String, Integer> map;
	TheHappyFarmer coll;

	@Before
	public void before() {
		map = Maps.newHashMap();
		map.put("keyWithoutValue", null);
		map.put("keyWithValue", 0);

		coll = TheHappyFarmer.of()
				.harvest("apple", null)
				.harvest("banana", 10);
	}

	@Test
	public void shouldBeSerializable() throws Exception {
		assertThat(Optional.class.getInterfaces()).contains(Serializable.class);
	}

	@Test(expected = NullPointerException.class)
	public void mapWithoutKey() throws Exception {
		Integer expected = map.get("yo");

		assertThat(expected).isNull();

		expected.intValue();

		failBecauseExceptionWasNotThrown(NullPointerException.class);
	}

	@Test(expected = NullPointerException.class)
	public void mapWithKeyButNullValue() throws Exception {
		Integer expected = map.get("keyWithoutValue");

		assertThat(expected).isNull();

		expected.intValue();

		failBecauseExceptionWasNotThrown(NullPointerException.class);
	}

	@Test(expected = IllegalStateException.class)
	public void mapWithOptional_bad() throws Exception {
		final Map<String, Optional<Integer>> badMap = Maps.newHashMap();
		badMap.put("keyWithoutValue", Optional.absent());
		Optional<Integer> expectNoKey = badMap.get("yo");

		// OMG
		assertThat(expectNoKey).isNull();

		Optional<Integer> expectNoValue = badMap.get("keyWithoutValue");

		assertThat(expectNoValue).isAbsent();

		expectNoValue.get();

		failBecauseExceptionWasNotThrown(IllegalStateException.class);
	}

	@Test
	public void mapWithOptional_better() throws Exception {
		Optional<Integer> expectNoKey = coll.pickOut("pear");

		assertThat(expectNoKey).isAbsent();

		Optional<Integer> expectNoValue = coll.pickOut("apple");

		assertThat(expectNoValue).isAbsent();

		Optional<Integer> expectValue = coll.pickOut("banana");
		
		if (expectValue.isPresent()) {
			// Do something
			System.out.println(expectValue.get());
		} else {
			// Do else
		}

		assertThat(expectValue).isPresent().contains(10);
	}

}
