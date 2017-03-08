package jug.swarm.swarmdemo.cdi.config;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Patterns {

  public static Predicate<String> matches(String regex) {
    return input -> Pattern.compile(regex).matcher(input).matches();
  }

  public static <E> Predicate<E> everTrue() {
    return s -> true;
  }

  public static <E> Predicate<E> everFalse() {
    return s -> false;
  }

  private Patterns() {
    throw new AssertionError("Patterns");
  }
}
