package jug.swarm.swarmdemo.rest;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/metrics")
public class MetricsResource {

  @Inject
  private MetricRegistry registry;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getRegistry(@QueryParam("name") String name) {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ConsoleReporter.forRegistry(registry).outputTo(new PrintStream(baos))
        .convertRatesTo(TimeUnit.MINUTES).filter((n, m) -> name == null || n.equals(name))
        .build().report();
    return new String(baos.toByteArray(), StandardCharsets.UTF_8);
  }

}
