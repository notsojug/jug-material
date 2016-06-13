package jug.streams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import com.google.common.base.Throwables;

public class ParallelStreamTest {
    @Test
	public void parallelStreamPrint() throws Exception {
		/*
		 * the computation of this range is done in a paralllel way. Try
		 * launching this test, you'll see the numbers printed in a random order
		 */
        IntStream.range(1, 100)
                .parallel()
                .forEach(System.out::println);
    }

    @Test
    public void parallelStream_toList() throws Exception {
		List<Integer> list = IntStream.range(1, 100)
				.parallel()
				.peek(System.out::println)
				.boxed()
				.collect(Collectors.toList());

		System.out.println("------------");

		/*
		 * the result is ordered, the computation is not (see the initial
		 * printout of the test)
		 */
		list.forEach(System.out::println);
	}
    
	@Test
	public void findFirst_shouldFindFirstOver5() throws Exception {
		/*
		 * find first lets you find the first element that satisfies the
		 * predicate
		 */
		OptionalInt first = IntStream.range(1, 100)
				.filter(x -> x % 5 == 0)
				.findFirst();
		assertThat(first).isNotEmpty().hasValue(5);
	}
	
	
	@Test
	public void findAny_shouldFindAnyOver5() throws Exception {

		OptionalInt any = IntStream.range(1, 100)
				.parallel()
				.filter(x -> x > 5)
				.findAny();
		
		/*
		 * notice the difference between find first and find any: the following
		 * line will print a number greater than 5, but can be either 6 or any
		 * other.
		 * 
		 * This effect is produced by the user of the parallel() call
		 */
		any.ifPresent(System.out::println);
		
		assertThat(any)
			.isNotEmpty();
	}
	
	
	static int expensiveComputation(int x){
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			Throwables.propagate(e);
		}
		
		return x*2;
	}
	
	//@Test // temporarily disabled because this 
	public void map_shouldTakeLessThanSequencially() throws Exception {
		long start = System.nanoTime();
		// this should take 100*0.2 seconds to compute sequentially.
		List<Integer> list = IntStream.range(1, 100)
				.parallel()
				.map(ParallelStreamTest::expensiveComputation)
				.boxed()
				.collect(Collectors.toList());
		
		long difference = System.nanoTime() - start;
		
		// list should have the right elements
		assertThat(list)
			.contains(198,196) // the last elements exist
			.doesNotContain(1, 5, 7) // even number do not exist
			.allMatch(x -> x % 2 == 0); // all numbers are even
		
		// on a parallel processor, this should take less than 20 secs...
		assertThat(difference).isLessThan(20L*1000000000L);
	}
	
}
