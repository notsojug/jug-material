package jug.injectintegrationtests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.assertj.core.api.Condition;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jug.inject.Database;
import jug.inject.UserManager;

@RunWith(Arquillian.class)
public class InjectionTest {
	@Inject
	UserManager userManager;
	
	@Inject
	Database database;
	
	@Test
	public void shouldHaveAnInstance() throws Exception {
		assertThat(userManager).isNotNull();
	}
	
	@Test
	public void databaseShouldBeTheSameInstanceEverywhere() throws Exception {
		assertThat(userManager)
			.extracting("dbAddUserCommand","dbDeleteUserCommand","dbGetUserCommand")
			.extracting("database")
			.areExactly(3, new Condition<Object>(o->o==database, "each database referece should be the same as the injected in test class"));
	}
	
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackage(UserManager.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
