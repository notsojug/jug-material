package jug.inject;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Rule;
import org.junit.Test;

public class ABetterTest {
	
	private static class AComplexObject {
		@Inject
		UserManager um;
		
		boolean isUserManagerPresent() {
			return um != null;
		}
		
		UserManager getUserManager() {
			return um;
		}
	}

	@Rule
	public EasyMockRule easyMock = new EasyMockRule(this);
	
	@Mock
	UserManager userManagerMock;
	
	@TestSubject
	AComplexObject cut = new AComplexObject(); //class under test
	
	@Test
	public void shouldHaveAUserManagerAtRuntime() throws Exception {
		assertThat(cut.isUserManagerPresent()).isTrue();
	}
	
	@Test
	public void shouldBeAnEasyMockProxy() throws Exception {
		UserManager userManager = cut.getUserManager();
		// EasyMock overrides toString() in mocked classes, so the presence of
		// "EasyMock" in toString() shows that this is indeed a mocked class  
		assertThat(userManager.toString()).contains("EasyMock");
	}
}
