package jug.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CapturingLambdaTest {
	List<Integer> integers;

	final int someConstant = 4;

	@Before
	public void setUp() {
		integers = Lists.newArrayList(1, 5, 7, 8, 10);
	}

	@Test
	public void shouldMultiplyByLocalVariable() throws Exception {
		int n = 2;
		Function<Integer, Integer> function = i -> i * n;
		// uncommenting the following line will result in a compilation error on the line above.
//		n = 3;
		List<Integer> transformed = ListUtils.transformList(integers, function);
		assertThat(transformed).contains(2, 10, 14);
	}

	@Test
	public void shouldMultiplyByField() throws Exception {
		List<Integer> transformed = ListUtils.transformList(integers, i -> i * someConstant);
		assertThat(transformed).contains(4, 20, 28);
	}
}
