package jug.swarm.swarmdemo;

import jug.swarm.swarmdemo.cdi.config.EagerExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.container.DeploymentException;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.undertow.UndertowFraction;

public class Main {

  public static void main(String[] args) throws Exception {
    // I'd like swarm
    Swarm container = new Swarm();
    container.withXmlConfig(Main.class.getClassLoader().getResource("standalone.xml"));

    // Please configure also AJP functionalities
    container.fraction(UndertowFraction.createDefaultAndEnableAJPFraction());

    // Start the container
    container.start();

    // Build an archive (war)
    JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class)
        .addPackages(true, "jug.swarm.swarmdemo.cdi").addPackages(true, "jug.swarm.swarmdemo.rest")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsResource(new StringAsset(EagerExtension.class.getCanonicalName()),
            "/META-INF/services/javax.enterprise.inject.spi.Extension")
        .addAllDependencies();

    // Deploy the archive, please
    try {
      container.deploy(jaxrsArchive);
    } catch (DeploymentException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

}
