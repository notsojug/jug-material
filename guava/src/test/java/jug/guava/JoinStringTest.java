package jug.guava;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.common.base.Joiner;

public class JoinStringTest {
	
	private String join(String... all) {
		StringBuilder sb = new StringBuilder();
		for(String one :  all){
			if(one!=null){
				sb.append(one);
				sb.append(",");
			}
		}		
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}
	
	@Test
	public void shouldConcatenateTwoStrings() throws Exception {
		assertThat(join("pippo","pluto")).isEqualTo("pippo,pluto");
	}
	
	@Test
	public void shouldConcatenateTwoStringsAndANull() throws Exception {
		assertThat(join("pippo","pluto", null)).isEqualTo("pippo,pluto");
	}
	
	private String join_guava(String... all){
		return Joiner.on(",").skipNulls().join(all);
	}
	
	@Test
	public void shouldConcatenateTwoStrings_guava() throws Exception {
		assertThat(join_guava("pippo", "pluto"))
			.isEqualTo("pippo,pluto");
	}
	
	@Test
	public void shouldConcatenateTwoStringsAndANull_guava() throws Exception {
		assertThat(join_guava("pippo","pluto",null))
			.isEqualTo("pippo,pluto");
	}

}
