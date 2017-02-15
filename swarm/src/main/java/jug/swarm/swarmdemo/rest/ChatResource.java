package jug.swarm.swarmdemo.rest;

import javaslang.Tuple2;
import jug.swarm.swarmdemo.cdi.ResponseCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ChatResource implements Chat {

  private static final Logger LOGGER = LoggerFactory.getLogger(ChatResource.class);

  @Inject
  ResponseCollector collector;

  @Override
  public List<Tuple2<String, String>> questions() {
    return collector.questions();
  }

  @Override
  public void question(String key, String question, final AsyncResponse asyncResponse) {
    collector.put(key, question, asyncResponse);
    LOGGER.info("Collected request " + key);
  }

  @Override
  public Response response(String key, String response) {
    // Please don't imitate
    return collector.get(key).map(ar -> {
      ar.resume(response);
      return Response.ok().build();
    }).orElse(Response.status(Status.NOT_FOUND).build());
  }

}
