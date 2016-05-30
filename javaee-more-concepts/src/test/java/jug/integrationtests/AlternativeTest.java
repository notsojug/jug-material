package jug.integrationtests;

import static jug.base.Assertions.assertThat;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.*;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jug.alternative.memory.InMemoryUserRepo;
import jug.base.AddUserCommand;
import jug.base.UserManager;

@RunWith(Arquillian.class)
public class AlternativeTest {
	
	@Inject
	UserManager userManager;
	
	@Inject
	AddUserCommand addUserCommand;
	
	@Test
	public void shouldHaveAnInstance() throws Exception {
		assertThat(userManager).isNotNull();
	}
	
	@Test
	public void shouldBeOfInMemoryRepository() throws Exception {
		assertThat(addUserCommand).isInstanceOf(InMemoryUserRepo.class);
	}
	
//	@Deployment
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap.create(JavaArchive.class)
//            .addPackages(true, UserManager.class.getPackage())
//            .addPackages(true, InMemoryUserRepo.class.getPackage())
//            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }
	
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(true, UserManager.class.getPackage())
            .addPackages(true, InMemoryUserRepo.class.getPackage())
            .addAsManifestResource(new StringAsset("<beans xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" + 
            		"   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
            		"   xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee\n" + 
            		"      http://java.sun.com/xml/ns/javaee/beans_1_0.xsd\">\n"
            		+ "<alternatives>\n"
            		+ "<class>jug.alternative.memory.InMemoryUserRepo</class>\n"
            		+ "</alternatives>\n" + 
            		"</beans>"), "beans.xml");
    }
}
