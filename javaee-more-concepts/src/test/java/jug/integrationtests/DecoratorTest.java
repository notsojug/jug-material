package jug.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jug.decorator.Client;
import jug.decorator.SomeClient;

@RunWith(Arquillian.class)
public class DecoratorTest {
	@Inject
	Client cut;

	@Test
	public void shouldCallDecorator() throws Exception {
		assertThat(cut.getValueFromTheInternet("oh")).isEqualTo("delayed=42");
	}
	
	@Test
	public void shouldCallSleep() throws Exception {
		assertThat(cut.getValueFromTheInternet("yo")).isEqualTo("delayed=42");
	}

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
				.addPackages(true, SomeClient.class.getPackage())
				.addAsManifestResource(
						new StringAsset("<beans xmlns=\"http://java.sun.com/xml/ns/javaee\"\n"
								+ "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
								+ "   xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee\n"
								+ "      http://java.sun.com/xml/ns/javaee/beans_1_0.xsd\">\n" 
								+ "<decorators>\n"
								+ "		<class>jug.decorator.SomeDelayedClient</class>\n" 
								+ "</decorators>\n" 
								+ "</beans>"),
						"beans.xml");
	}
}
