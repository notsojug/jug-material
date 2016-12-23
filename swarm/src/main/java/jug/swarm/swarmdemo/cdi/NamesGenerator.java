package jug.swarm.swarmdemo.cdi;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.annotation.Metric;
import com.codahale.metrics.annotation.Timed;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.inject.Inject;

@Generator
public class NamesGenerator implements Supplier<String> {

  /** Steve Wozniak is not boring */
  private static final Predicate<String> NOT_WOZ = not("boring_wozniak"::equals);

  @Inject
  @Adjectives
  private Supplier<String> adjectivesSupplier;

  @Inject
  @Names
  private Supplier<String> namesSupplier;

  @Inject
  @Metric(name = "length", absolute = true)
  private Histogram length;

  @Override
  @Timed(name = "time", absolute = true)
  public String get() {
    return Stream
        .generate(() -> String.format("%s_%s", adjectivesSupplier.get(), namesSupplier.get()))
        .filter(NOT_WOZ).peek(n -> length.update(n.length())).findAny()
        .orElseThrow(IllegalStateException::new);
  }

  private static <E> Predicate<E> not(Predicate<E> p) {
    return p.negate();
  }

}
