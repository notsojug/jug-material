package jug.cdi.scopes;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class RandomServletTest {

  @Deployment(testable = false)
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
        .addPackage(RandomServlet.class.getPackage())
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @ArquillianResource
  URL baseUrl;

  @Drone
  WebDriver browser;

  @Test
  public void shouldBeRandom() throws Exception {
    browser.get(new URL(baseUrl, "/test/random").toExternalForm());
    String firstPageSource = browser.getPageSource();
    browser.get(new URL(baseUrl, "/test/random").toExternalForm());
    String secondPageSource = browser.getPageSource();
    
    assertThat(firstPageSource).isNotEqualTo(secondPageSource);
  }

  @Test
  public void shouldHaveSameName() throws Exception {
    browser.get(new URL(baseUrl, "/test/random?name").toExternalForm());
    String firstPageSource = browser.getPageSource();
    browser.get(new URL(baseUrl, "/test/random?name").toExternalForm());
    String secondPageSource = browser.getPageSource();
    
    assertThat(firstPageSource).isEqualTo(secondPageSource);
  }

}
