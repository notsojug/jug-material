package jug.swarm.swarmdemo.rest;

import jug.swarm.swarmdemo.cdi.Generator;

import java.util.function.Supplier;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Kudos to https://github.com/docker/docker/blob/master/pkg/namesgenerator/names-generator.go
 */
@Path("/names-generator")
public class NamesResource {

  @Inject
  @Generator
  private Supplier<String> namesGenerator;

  @GET
  @Produces("text/plain")
  public String doGet() {
    return namesGenerator.get();
  }

}
