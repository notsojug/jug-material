package jug.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jug.base.AddUserCommand;

@RunWith(Arquillian.class)
public class InjectionPointTest {

	@Inject
	Generator generator;

	@Inject @ConfiguredForTest
	AddUserCommand addUser;

	@ApplicationScoped
	public static class Generator {
		private InjectionPoint latest;
		
		public InjectionPoint getLatest() {
			return latest;
		}

		@Produces @ConfiguredForTest
		AddUserCommand generateAddUserCommand(InjectionPoint ip) {
			System.out.println("called");
			this.latest = ip;
			return null;
		}
	}
	
	@Test
	public void producerShouldHaveBeenCalledByThisTestClass() throws Exception {
		InjectionPoint injectionPoint = generator.getLatest();
		Member injectDestination= injectionPoint.getMember();
		
		// you can get the class of the object that is requesting injection.
		assertThat(injectDestination.getDeclaringClass()).isSameAs(this.getClass());
		// the name of the field/parameter in which to inject
		assertThat(injectDestination.getName()).isEqualTo("addUser");
	}
	
	@SuppressWarnings("serial")
	@Test
	public void injectedClassShouldBeOfSpecificTypeAndAnnotations() throws Exception {
		InjectionPoint injectionPoint = generator.getLatest();
		Annotated annotated = injectionPoint.getAnnotated();
		// you can infer the requested type for injection (for generic inception or for specific interfaces)
		assertThat(annotated.getBaseType()).isEqualTo(AddUserCommand.class);
		
		// you can get the annotations of the injected-to-be field/parameter
		Set<Annotation> annotations = annotated.getAnnotations();
		assertThat(isAnnotationPresentInSet(new AnnotationLiteral<ConfiguredForTest>(){}, annotations))
			.as("Annotation " + ConfiguredForTest.class.getSimpleName() + " should be present in injection point")
			.isTrue();
		
	}
	

	private boolean isAnnotationPresentInSet(AnnotationLiteral<ConfiguredForTest> annotationLiteral,
			Set<Annotation> annotations) {
		for (Annotation one : annotations) {
			if (annotationLiteral.equals(one)) {
				return true;
			}
		}
		return false;
	}

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
				.addPackages(true, AddUserCommand.class.getPackage())
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
}
