package jug.lambda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;
import java.util.function.Function;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MethodReferenceTest {
	List<Integer> integers;

	@Before
	public void setUp() {
		integers = Lists.newArrayList(1, 5, 7, 8, 10);
	}

	private static boolean isEven(Integer t) {
		return t.intValue() % 2 == 0;
	}

	@Test
	public void shouldFilterEvenIntegers() throws Exception {
		List<Integer> filtered = ListUtils.filterList(integers, MethodReferenceTest::isEven);
		assertThat(filtered).containsExactlyInAnyOrder(8, 10);
	}

	private static int multiplyByTwo(int t) {
		return t * 2;
	}

	@Test
	public void shouldFilterAndMultiplyByTwo() throws Exception {
		List<Integer> filtered = ListUtils.filterList(integers, MethodReferenceTest::isEven);
		Function<Integer, Integer> function = MethodReferenceTest::multiplyByTwo;
		List<Integer> tranformed = ListUtils.transformList(filtered, function);
		assertThat(tranformed).containsExactlyInAnyOrder(16, 20);
	}

	@Test
	public void shouldConvertToString() throws Exception {
		List<String> transformed = ListUtils.transformList(integers, Object::toString);
		assertThat(transformed).contains("1", "5");
	}
	
	@Test
	public void shouldProduceAUselessList() throws Exception {
		Function<Integer, Boolean> function = integers::contains;
		List<Boolean> transformList = ListUtils.transformList(integers, function);
		assertThat(transformList).are(new Condition<>(t->t==true, "All values should be true"));
	}

	// @Test
	// public void shouldNotCompile() throws Exception {
	// List<String> transformed = ListUtils.transformList(integers,
	// Object::toString);
	// ListUtils.transformList(transformed, MethodReferenceTest::isEven);
	// Fail.fail("should not compile");
	// }
}
