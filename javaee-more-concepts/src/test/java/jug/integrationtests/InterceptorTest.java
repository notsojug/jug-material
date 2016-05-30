package jug.integrationtests;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jug.interceptor.ClassToBeLogged;

@RunWith(Arquillian.class)
public class InterceptorTest {
	
	@Inject
	ClassToBeLogged cut;
	
	@Test
	public void shouldCallInterceptor() throws Exception {
		cut.someMethod("high", 5);
	}
	
	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(true, ClassToBeLogged.class.getPackage())
            .addAsManifestResource(new StringAsset("<beans xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" + 
            		"   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
            		"   xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee\n" + 
            		"      http://java.sun.com/xml/ns/javaee/beans_1_0.xsd\">\n"
            		+ "<interceptors>\n"
            		+ "<class>jug.interceptor.LoggedInterceptor</class>\n"
            		+ "</interceptors>\n" + 
            		"</beans>"), "beans.xml");
    }
}
