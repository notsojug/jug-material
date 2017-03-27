package jug.swarm.swarmdemo;

import jug.swarm.swarmdemo.cdi.config.EagerExtension;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.container.DeploymentException;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.undertow.UndertowFraction;

public class Main {

  @Option(name = "-j", aliases = { "--ajp" }, usage = "enable ajp configuration")
  private boolean ajp;

  @Option(name = "-x", aliases = { "--xml-config" }, usage = "xml configuration file")
  private File xmlConfiguration;

  public static void main(String[] args) throws Exception {
    new Main().doMain(args);
  }

  /**
   * @param args
   * @throws Exception
   */
  public void doMain(String[] args) throws Exception {
    CmdLineParser parser = new CmdLineParser(this);
    try {
      parser.parseArgument(args);
      System.out.println(toString());
      // I'd like swarm
      Swarm container = new Swarm();
      if (xmlConfiguration != null && xmlConfiguration.exists() && xmlConfiguration.isFile()
          && xmlConfiguration.canRead()) {
        container.withXmlConfig(xmlConfiguration.toURI().toURL());
      } else {
        container.withXmlConfig(Main.class.getClassLoader().getResource("standalone.xml"));
      }

      // Please configure also AJP functionalities
      final UndertowFraction undertowFraction = UndertowFraction.createDefaultFraction();
      container.fraction(ajp ? undertowFraction.enableAJP() : undertowFraction);

      // Start the container
      container.start();

      // Build an archive (war)
      JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class)
          .addPackages(true, "jug.swarm.swarmdemo.cdi")
          .addPackages(true, "jug.swarm.swarmdemo.rest")
          .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
          .addAsResource(new StringAsset(EagerExtension.class.getCanonicalName()),
              "/META-INF/services/javax.enterprise.inject.spi.Extension")
          .addAllDependencies();

      // Deploy the archive, please
      container.deploy(jaxrsArchive);
    } catch (CmdLineException e) {
      parser.printUsage(System.err);
    } catch (DeploymentException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  @Override
  public String toString() {
    return "Main [ajp=" + ajp + ", xmlConfiguration="
        + (xmlConfiguration == null ? null : xmlConfiguration.getAbsolutePath()) + "]";
  }

}
