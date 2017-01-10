package jug.swarm.swarmdemo.rest;

import static java.lang.Math.*;

import javaslang.control.Try;
import javaslang.control.Try.CheckedRunnable;
import javaslang.control.Try.CheckedSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloWorldEndpoint {

  private static final long COMPUTE_MILLIS = 50L;

  @SuppressWarnings("unused")
  private static final CheckedRunnable RUNNABLE_SLEEP = () -> Thread.sleep(COMPUTE_MILLIS);
  private static final CheckedSupplier<String> SUPPLIER =
      () -> String.valueOf(tan(atan(tan(atan(tan(atan(tan(atan(123456789.123456789)))))))));

  private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldEndpoint.class);

  @Resource
  ManagedExecutorService managedExecutorService;

  @GET
  @Produces("text/plain")
  public Response doGet() {
    return Response.ok("Hello from WildFly Swarm!").build();
  }

  @POST
  @Produces("text/plain")
  public Response doPost(@FormParam("n") String n, @FormParam("p") String p) {
    LOGGER.info(params(n, p));
    return Response.ok(Try.of(SUPPLIER).getOrElse("notan")).build();
  }

  @POST
  @Path("/async")
  @Produces("text/plain")
  public void doPostAsync(@FormParam("n") String n, @FormParam("p") String p,
      @Suspended final AsyncResponse asyncResponse) {
    new Thread(() -> {
      LOGGER.info(params(n, p));
      asyncResponse.resume(Response.ok(Try.of(SUPPLIER).getOrElse("notan")).build());
    }).start();
  }

  @POST
  @Path("/async/managed")
  @Produces("text/plain")
  public void doPostManaged(@FormParam("n") String n, @FormParam("p") String p,
      @Suspended final AsyncResponse asyncResponse) {
    managedExecutorService.execute(() -> {
      LOGGER.info(params(n, p));
      asyncResponse.resume(Response.ok(Try.of(SUPPLIER).getOrElse("notan")).build());
    });
  }

  private static String params(String n, String p) {
    return "[n=" + n + "][p=" + p + "]";
  }
}
