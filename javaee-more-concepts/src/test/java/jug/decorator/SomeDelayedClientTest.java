package jug.decorator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Rule;
import org.junit.Test;

public class SomeDelayedClientTest {

	@Rule
	public EasyMockRule rule = new EasyMockRule(this);

	@Mock
	Client mock;

	@TestSubject
	SomeDelayedClient cut = new SomeDelayedClient() {

		@Override
		public String putValueToTheInternet(String id, String value) {
			return "doneFromInner";
		}
	};

	@Test
	public void nonOverriddenMethodShouldBehaveTheSame() throws Exception {
		assertThat(cut.putValueToTheInternet("id", "value")).isEqualTo("doneFromInner");
	}

	@Test
	public void overriddenMethodShouldHaveDifferentPrefix() throws Exception {
		assertThat(cut.getValueFromTheInternet("yo")).startsWith("delayed");
	}

	@Test
	public void withMocks() throws Exception {
		expect(mock.getValueFromTheInternet("yo")).andReturn("mama");
		replay(mock);
		assertThat(cut.getValueFromTheInternet("yo")).isEqualTo("delayed=mama");
		verify(mock);
	}
}
