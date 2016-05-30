package jug.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

import jug.base.AddUserCommand;

@RunWith(Arquillian.class)
public class InjectionPointTest2 {

	@Inject
	Generator generator;

	@Inject
	@SelectedForTest("vanilla")
	AddUserCommand vanillaAddUser;

	@Inject
	@SelectedForTest("chocolate")
	AddUserCommand chocolateAddUser;

	@ApplicationScoped
	public static class Generator {
		private List<String> requested = Lists.newArrayList();

		public List<String> getRequested() {
			return requested;
		}

		@Produces
		@SelectedForTest("")
		AddUserCommand generateAddUserCommand(InjectionPoint ip) {
			SelectedForTest selectedForTest = ip.getAnnotated().getAnnotation(SelectedForTest.class);
			System.out.println("called with annotation value=" + selectedForTest.value());
			this.requested.add(selectedForTest.value());
			return null;
		}
	}

	@Test
	public void testName() throws Exception {
		assertThat(generator.getRequested()).contains("vanilla", "chocolate");
	}

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
				.addPackages(true, AddUserCommand.class.getPackage())
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
}
