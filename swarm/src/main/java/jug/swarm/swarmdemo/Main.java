package jug.swarm.swarmdemo;

import jug.swarm.swarmdemo.rest.AsynchronousResource;
import jug.swarm.swarmdemo.rest.HelloWorldEndpoint;
import jug.swarm.swarmdemo.rest.MetricsResource;
import jug.swarm.swarmdemo.rest.NamesResource;
import jug.swarm.swarmdemo.rest.RuntimeExceptionMapper;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.wildfly.swarm.Swarm;
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
            .addResource(HelloWorldEndpoint.class)
            .addResource(NamesResource.class)
            .addResource(MetricsResource.class)
            .addResource(AsynchronousResource.class)
            .addResource(RuntimeExceptionMapper.class)
            .addPackages(true, "jug.swarm.swarmdemo.cdi")
            .addPackages(true, "jug.swarm.swarmdemo.rest.async")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAllDependencies();

    // Deploy the archive, please
    container.deploy(jaxrsArchive);
  }

}
