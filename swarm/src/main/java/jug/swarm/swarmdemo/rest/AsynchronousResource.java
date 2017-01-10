package jug.swarm.swarmdemo.rest;

import jug.swarm.swarmdemo.cdi.SlowGenerator;

import java.util.function.Supplier;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
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
    System.out.println("GET: " + Thread.currentThread().getName());
    asyncResponse.register((CompletionCallback) t -> System.out.println("CompletionCallback: " + t),
        (ConnectionCallback) ar -> System.out.println("CompletionCallback: " + ar));

    managedExecutorService.submit(() -> {
      System.out.println("Compute: " + Thread.currentThread().getName());
      asyncResponse
          .resume(Response.ok(namesGenerator.get() + "_" + nullToEmpty(param)).build());
    });
  }

  private static String nullToEmpty(String param) {
    if (param == null) {
      return EMPTY;
    } else {
      return param;
    }
  }
}
