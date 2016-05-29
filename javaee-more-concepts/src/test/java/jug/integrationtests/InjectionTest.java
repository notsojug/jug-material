package jug.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.assertj.core.api.Condition;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jug.base.UserManager;
import jug.base.db.Database;

@RunWith(Arquillian.class)
public class InjectionTest {
	@Inject
	UserManager userManager;
	
	@Inject
	Database database;
	
	@Test
	public void shouldHaveAnInstance() throws Exception {
		assertThat(userManager).isNotNull();
		assertThat(database).isNotNull();
	}
	
	@Test
	public void databaseShouldBeTheSameInstanceEverywhere() throws Exception {
		assertThat(userManager)
			.extracting("addUserCommand","deleteUserCommand","getUserCommand")
			.extracting("database")
			.areExactly(3, new Condition<Object>(o->o==database, "each database referece should be the same as the injected in test class"));
	}
	
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(true, UserManager.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
