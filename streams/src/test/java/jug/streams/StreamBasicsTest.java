package jug.streams;

import static jug.streams.ListUtils.list;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class StreamBasicsTest {
	
	List<Integer> integers;

	@Before
	public void setUp() {
		integers = Lists.newArrayList(1, 5, 7, 8, 10);
	}
	
	@Test
	public void filter_shouldHaveOnlyEvenElements() throws Exception {
		/*
		 * filter lets you keep only elements that satisfy the predicate
		 */
		List<Integer> result = integers.stream()
				.filter(x -> x % 2 == 0)
				.collect(Collectors.toList());
		
		assertThat(result).containsExactlyInAnyOrder(8,10);
	}
	

	
	@Test
	public void map_shouldTransformToListofString() throws Exception {
		/*
		 * map lets you transform the data from the stream
		 */
		List<String> strings = integers.stream()
				.map(Object::toString)
				.collect(Collectors.toList());
		assertThat(strings).contains("1", "5", "7", "8", "10");
	}

	@Test
	public void map_shouldHaveTheElementsDoubled() throws Exception {
		/*
		 * map lets you transform the data from the stream
		 */
		List<Integer> result = integers.stream()
				.map(x -> x * 2)
				.collect(Collectors.toList());

		assertThat(result).contains(2, 10, 14, 16, 20);
	}
	
	@Test
	public void groupBy_shouldSeparate() throws Exception {
		List<String> strings = Lists.newArrayList("Never","gonna","give","you","up");
		
		/*
		 * group by lets you create an map from the data, using a key of choice
		 * via a lambda
		 */
		Map<Integer, List<String>> collect = strings.stream()
				.collect(Collectors.groupingBy(x->((String)x).length()));
		
		assertThat(collect)
			.containsOnlyKeys(2,3,4,5)
			.containsEntry(5, list("Never", "gonna"))
			.containsEntry(4, list("give"))
			.containsEntry(3, list("you"))
			.containsEntry(2, list("up"));
	}

	@Test
	public void partition_shouldSeparateEvenAndOdds() throws Exception {
		Map<Boolean, List<Integer>> evenAndOdds = integers.stream()
				.collect(Collectors.partitioningBy(x -> x % 2 == 0));
		
		assertThat(evenAndOdds.get(true)).contains(8,10);
		assertThat(evenAndOdds.get(false)).contains(1,5,7);
	}
	
	@Test
	public void flatMap_shouldResultInAPredictableList() throws Exception {
		ArrayList<ValueHolder> valueHolders = Lists.newArrayList(ValueHolder.of(list("Never","gonna")), 
				ValueHolder.empty(),
				ValueHolder.of(list("give","you")),
				ValueHolder.of(list("up")));
		/*
		 * Flatmap lets you return a stream from a lambda, and "pumps" that
		 * stream into the main one; So the "flat" a stream of streams, into a
		 * simple stream. java.util.Optional has a similar method.
		 */
		String result = valueHolders.stream()
			.flatMap(vh -> vh.getList().stream())
			.collect(Collectors.joining(" "));
		
		assertThat(result).isEqualTo("Never gonna give you up");
	}
	
	@Test
	public void limit_shouldLimitToSize() throws Exception {
		List<Integer> limited = integers.stream()
			.limit(2)
			.collect(Collectors.toList());
		
		assertThat(limited).hasSize(2);
	}
	
	@Test
	public void limit_shouldStopAtEndOfList() throws Exception {
		List<Integer> limited = integers.stream()
				.limit(2000)
				.collect(Collectors.toList());
			
		// limit(N) does not pad the result, it just stop the stream after N
		// elements.
		assertThat(limited).hasSize(5);
	}
	
	@Test
	public void allMatch_allIntegersShouldSatisfyPredicate() throws Exception {
		boolean allMatch = integers.stream().allMatch(x -> x < 100);

		assertThat(allMatch).isTrue();
	}
}
