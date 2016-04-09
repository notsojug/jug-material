package jug.guava;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class JoinerTest {
	@Test
	public void shouldConcatenateTwoStrings() throws Exception {
		String join = Joiner.on(",").join("pippo", "pluto");
		assertThat(join).isEqualTo("pippo,pluto");
	}
	
	@Test
	public void shouldConcatenateTwoStringsAndANull() throws Exception {
		ArrayList<String> newArrayList = Lists.newArrayList("pippo","pluto",null);
		String join = Joiner.on(",").skipNulls().join(newArrayList);
		assertThat(join).isEqualTo("pippo,pluto");
	}
}
