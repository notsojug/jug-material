package jug.swarm.swarmdemo.rest;

import jug.swarm.swarmdemo.cdi.TheClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/config")
public class ConfigResource {

  @Inject
  TheClient ciccio;

  @GET
  public String getProperty() {
    return ciccio.toString();
  }
}
