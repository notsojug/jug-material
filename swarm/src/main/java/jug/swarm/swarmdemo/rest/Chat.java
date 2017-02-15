package jug.swarm.swarmdemo.rest;

import javaslang.Tuple2;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/chat")
public interface Chat {

  @GET
  @Path("/questions")
  @Produces(MediaType.APPLICATION_JSON)
  List<Tuple2<String, String>> questions();

  @POST
  @Path("/question")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  void question(@NotNull @FormParam("key") String key,
      @NotNull @FormParam("question") String question, @Suspended AsyncResponse asyncResponse);

  @POST
  @Path("/response/{key}")
  @Consumes(MediaType.TEXT_PLAIN)
  Response response(@NotNull @PathParam("key") String key, @NotNull String response);

}
