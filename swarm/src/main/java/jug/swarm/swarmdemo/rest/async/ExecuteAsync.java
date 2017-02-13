package jug.swarm.swarmdemo.rest.async;

import javaslang.control.Try.CheckedSupplier;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ExecuteAsync {

  private static final Response SERVICE_UNAVAILABLE =
      Response.status(Status.SERVICE_UNAVAILABLE).build();
  private static final TimeoutHandler SERVICE_UNAVAILABLE_HANDLER =
      ar -> ar.resume(SERVICE_UNAVAILABLE);

  private final AsyncResponse asyncResponse;
  private final ExecutorService executorService;

  private ExecuteAsync(AsyncResponse asyncResponse, ExecutorService executorService,
      long timeoutMillis) {
    this.asyncResponse = Objects.requireNonNull(asyncResponse, "asyncResponse required non null");
    this.executorService =
        Objects.requireNonNull(executorService, "executorService required non null");
    this.asyncResponse.setTimeout(timeoutMillis, TimeUnit.MILLISECONDS);
    this.asyncResponse.setTimeoutHandler(SERVICE_UNAVAILABLE_HANDLER);
  }

  public static ExecuteAsync with(AsyncResponse asyncResponse, long timeoutMillis,
      ExecutorService executorService) {
    return new ExecuteAsync(asyncResponse, executorService, timeoutMillis);
  }

  public void execute(CheckedSupplier<Response> supplier) {
    executorService.submit(() -> {
      try {
        asyncResponse.resume(supplier.get());
      } catch (Throwable th) {
        if (th instanceof Error) {
          throw (Error) th;
        }

        if (th instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }

        asyncResponse.resume(th);
      }
    });
  }

}
