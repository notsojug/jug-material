package jug.swarm.swarmdemo.rest;

import javaslang.control.Try;
import jug.swarm.swarmdemo.cdi.SlowGenerator;
import jug.swarm.swarmdemo.rest.async.ExecuteAroundAsync;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

@Path("/asyncresource")
public class AsynchronousResource {

  private static final String EMPTY = "";

  @Resource
  ManagedExecutorService managedExecutorService;

  @Inject
  @SlowGenerator
  private Supplier<String> namesGenerator;

  @GET
  @Produces("text/plain")
  public void asyncRestMethod(@QueryParam("param") final String param,
      @Suspended final AsyncResponse asyncResponse) {
    asyncResponse.setTimeout(1500, TimeUnit.MILLISECONDS);
    asyncResponse.setTimeoutHandler(ar -> ar.resume(Response.ok("late").build()));

    managedExecutorService.submit(() -> {
      System.out.println("Compute: " + Thread.currentThread().getName());
      asyncResponse.resume(
          Try.of(() -> Response.ok(namesGenerator.get() + nullToEmpty(param)).build()).getOrElseGet(
              exception -> Response.status(500).entity(exception.getMessage()).build()));
    });
  }

  @GET
  @Path("/executearound")
  @Produces("text/plain")
  public void asyncRestMethodExecute(@QueryParam("param") final String param,
      @Suspended final AsyncResponse asyncResponse) {
    ExecuteAroundAsync.of(asyncResponse, 1500L, managedExecutorService).execute(() -> {
      return Response.ok(namesGenerator.get() + nullToEmpty(param)).build();
    });
  }

  private static String nullToEmpty(String param) {
    if (param == null) {
      return EMPTY;
    } else {
      return "_" + param;
    }
  }
}
