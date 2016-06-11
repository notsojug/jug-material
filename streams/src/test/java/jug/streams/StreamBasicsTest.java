package jug.streams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
		List<Integer> result = integers.stream()
			.filter(x->x%2==0)
			.collect(Collectors.toList());
		
		assertThat(result).containsExactlyInAnyOrder(8,10);
	}
	
	@Test
	public void findFirst_shouldFindFirstOver5() throws Exception {
		Optional<Integer> first = integers.stream()
				.filter(x->x>5)
				.findFirst();
		assertThat(first).isNotEmpty().hasValue(7);
	}
	
	
	@Test
	public void findAny_shouldFindAnyOver5() throws Exception {
		Optional<Integer> any = integers.stream()
				.filter(x->x>5)
				.findAny();
		assertThat(any)
			.isNotEmpty()
			.hasValueSatisfying(x -> assertThat(x).isIn(7, 8, 10));
	}
	
	@Test
	public void map_shouldTransformToListofString() throws Exception {
		List<String> strings = integers.stream()
				.map(Object::toString)
				.collect(Collectors.toList());
		assertThat(strings).contains("1","5","7","8","10");
	}

	@Test
	public void map_shouldHaveTheElementsDoubled() throws Exception {
		List<Integer> result = integers.stream()
			.map(x->x*2)
			.collect(Collectors.toList());
		
		assertThat(result).contains(2,10,14,16,20);
	}
	
	@Test
	public void partition_shouldSeparateEvenAndOdds() throws Exception {
		Map<Boolean, List<Integer>> evenAndOdds = integers.stream()
				.collect(Collectors.partitioningBy(x -> x % 2 == 0));
		
		assertThat(evenAndOdds.get(true)).contains(8,10);
		assertThat(evenAndOdds.get(false)).contains(1,5,7);
	}
	
	@Test
	public void groupBy_shouldSeparate() throws Exception {
		List<String> strings = Lists.newArrayList("Never","gonna","give","you","up");
		Map<Integer, List<String>> collect = strings.stream().collect(Collectors.groupingBy(x->((String)x).length()));
		assertThat(collect)
			.containsOnlyKeys(2,3,4,5)
			.containsEntry(5, Lists.newArrayList("Never", "gonna"))
			.containsEntry(4, Lists.newArrayList("give"))
			.containsEntry(3, Lists.newArrayList("you"))
			.containsEntry(2, Lists.newArrayList("up"));
	}
}
