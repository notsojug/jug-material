package jug.inject3integrationtests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jug.inject3.Configured;
import jug.inject3.VariousConstructors;

@RunWith(Arquillian.class)
public class VariousConstructorsIT {
	
	@Inject
	VariousConstructors standard;
	
	@Inject
	@Configured
	VariousConstructors configured;
	
	@Test
	public void standardObjectShouldContainDefaultValue() throws Exception {
		assertThat(standard.getInner()).isEqualTo("my useless default inner value");
	}
	
	@Test
	public void configuredObjectShouldHaveDifferentInnerValues() throws Exception {
		assertThat(configured.getInner())
			.isEqualTo("factory and configured")
			.isNotEqualTo("my useless default inner value");
	}
	
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackage(VariousConstructors.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
