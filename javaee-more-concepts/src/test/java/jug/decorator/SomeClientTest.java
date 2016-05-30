package jug.decorator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SomeClientTest {
	Client cut = new SomeClient();

	@Test
	public void shouldReturn42() throws Exception {
		assertThat(cut.getValueFromTheInternet("yo")).isEqualTo("42");
	}
}
