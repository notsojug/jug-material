package jug.inject2integrationtests;


import static jug.inject.Assertions.assertThat;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jug.inject2.AbstractImmutableWithFactory;
import jug.inject2.OnlyWithFactory;
import jug.inject2.SomethingWithLotsOfConfiguration;

@RunWith(Arquillian.class)
public class InjectionTestsWithFactory {
	
	@Inject
	OnlyWithFactory something;
	
	@Inject
	AbstractImmutableWithFactory immutable;
	
	@Inject
	SomethingWithLotsOfConfiguration configured;
	
	@Test
	public void shouldBePresentAnWithExpectedID() throws Exception {
		assertThat(something)
			.isNotNull()
			.hasId("42");
	}
	
	@Test
	public void shouldHaveSameIdAsTheOther() throws Exception {
		assertThat(immutable)
			.hasId("46");
	}
	
	@Test
	public void shouldHaveAValidConfiguredObject() throws Exception {
		assertThat(configured).isNotNull();
	}
	
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackage(OnlyWithFactory.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
