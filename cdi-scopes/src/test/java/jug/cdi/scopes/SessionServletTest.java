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
public class SessionServletTest {

  @Deployment(testable = false)
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
        .addPackage(SessionServlet.class.getPackage())
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @ArquillianResource
  URL baseUrl;

  @Drone
  WebDriver browser;

  @Test
  @InSequence(1)
  public void firstSessionShouldZero() throws Exception {
    browser.get(new URL(baseUrl, "/test/session").toExternalForm());
    String pageSource = browser.getPageSource();
    assertThat(pageSource).isEqualTo("0");
  }

  @Test
  @InSequence(2)
  public void secondSessionShouldOne() throws Exception {
    browser.get(new URL(baseUrl, "/test/session").toExternalForm());
    String pageSource = browser.getPageSource();
    assertThat(pageSource).isEqualTo("1");
  }

  @Test
  @InSequence(3)
  public void firstRequestShouldZero() throws Exception {
    browser.get(new URL(baseUrl, "/test/request").toExternalForm());
    String pageSource = browser.getPageSource();
    assertThat(pageSource).isEqualTo("0");
  }

  @Test
  @InSequence(4)
  public void secondRequestShouldZero() throws Exception {
    browser.get(new URL(baseUrl, "/test/request").toExternalForm());
    String pageSource = browser.getPageSource();
    assertThat(pageSource).isEqualTo("0");
  }

  @Test
  @InSequence(5)
  public void thirdSessionShouldTwo() throws Exception {
    browser.get(new URL(baseUrl, "/test/session").toExternalForm());
    String pageSource = browser.getPageSource();
    assertThat(pageSource).isEqualTo("2");
  }
}
