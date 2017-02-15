package jug.swarm.swarmdemo.cdi;

import javaslang.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.AsyncResponse;

@ApplicationScoped
public class ResponseCollector {
  private final Map<String, Tuple2<String, AsyncResponse>> collector = new HashMap<>();

  public void put(String key, String question, AsyncResponse asyncResponse) {
    collector.put(key, new Tuple2<>(question, asyncResponse));
  }

  public List<Tuple2<String, String>> questions() {
    return collector.entrySet().stream().map(en -> {
      return new Tuple2<String, String>(en.getKey(), en.getValue()._1);
    }).collect(Collectors.toList());
  }

  public Optional<AsyncResponse> get(String key) {
    return Optional.ofNullable(collector.get(key)).map(t -> collector.remove(key)).map(Tuple2::_2);
  }
}
