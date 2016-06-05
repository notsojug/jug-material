package jug.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class LambdaTest {
	
	List<Integer> integers;
	
	@Before
	public void setUp(){
		integers = Lists.newArrayList(1, 5, 7, 8, 10);
	}
	
	private static <T> List<T> filterList(List<T> original, Predicate<T> filter){
		Objects.requireNonNull(original);
		Objects.requireNonNull(filter);
		List<T> result = Lists.newArrayList();
		for(T one : original){
			if(filter.test(one)){
				result.add(one);
			}
		}
		return result;
	}
	
	@Test
	public void shouldFilterWithAnonymousClass() throws Exception {
		assertThat(filterList(integers, new Predicate<Integer>() {
			@Override
			public boolean test(Integer t) {
				return t.intValue() % 2 == 0;
			}
		})).containsExactlyInAnyOrder(8,10);
	}
	
	@Test
	public void shouldFilterWithBasicLambda() throws Exception {
		assertThat(filterList(integers, (Integer t) -> {
			return t.intValue() % 2 == 0;
		})).containsExactlyInAnyOrder(8, 10);
	}
	
	@Test
	public void shouldFilterWithConcise() throws Exception {
		assertThat(filterList(integers, (Integer t) -> t.intValue() % 2 == 0))
			.containsExactlyInAnyOrder(8,10);
	}
	
	@Test
	public void shouldFilterWithEvenMoreConcise() throws Exception {
		assertThat(filterList(integers, t -> t.intValue() % 2 == 0))
			.containsExactlyInAnyOrder(8,10);
	}
}
