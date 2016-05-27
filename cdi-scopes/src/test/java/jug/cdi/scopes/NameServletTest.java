package jug.cdi.scopes;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class NameServletTest {
  
  private static final String NAME = "test_name";

  @Deployment(testable = false)
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
        .addPackage(NameSetterServlet.class.getPackage())
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @ArquillianResource
  URL baseUrl;

  @Drone
  WebDriver browser;

  @Test
  @InSequence(1)
  public void shouldBeUnknown() throws Exception {
    browser.get(new URL(baseUrl, "/test/myname").toExternalForm());
    String pageSourceWithName = browser.getPageSource();
    assertThat(pageSourceWithName).isEqualTo("Unknown User Name");
  }

  @Test
  @InSequence(2)
  public void shouldSetName() throws Exception {
    browser.get(new URL(baseUrl, "/test/name?userName=" + NAME).toExternalForm());
    String pageSourceWithoutName = browser.getPageSource();
    assertThat(pageSourceWithoutName).isNotEqualTo(NAME);
  }

  @Test
  @InSequence(3)
  public void shouldGetName() throws Exception {
    browser.get(new URL(baseUrl, "/test/myname").toExternalForm());
    String pageSourceWithName = browser.getPageSource();
    assertThat(pageSourceWithName).isEqualTo(NAME);
  }

}
