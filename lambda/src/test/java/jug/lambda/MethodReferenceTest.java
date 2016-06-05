package jug.lambda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;import javax.xml.crypto.dsig.Transform;

import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MethodReferenceTest {
List<Integer> integers;
	
	@Before
	public void setUp(){
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
	
	private static int multiplyByTwo(int t){
		return t*2;
	}

	@Test
	public void shouldFilterAndMultiplyByTwo() throws Exception {
		List<Integer> filtered = ListUtils.filterList(integers, MethodReferenceTest::isEven);
		List<Integer> tranformed = ListUtils.transformList(filtered, MethodReferenceTest::multiplyByTwo);
		assertThat(tranformed).containsExactlyInAnyOrder(16, 20);
	}

	@Test
	public void shouldConvertToString() throws Exception {
		List<String> transformed = ListUtils.transformList(integers, Object::toString);
		assertThat(transformed).contains("1", "5");
	}
	
//	@Test
//	public void shouldNotCompile() throws Exception {
//		List<String> transformed = ListUtils.transformList(integers, Object::toString);
//		ListUtils.transformList(transformed, MethodReferenceTest::isEven);
//		Fail.fail("should not compile");
//	}
}
