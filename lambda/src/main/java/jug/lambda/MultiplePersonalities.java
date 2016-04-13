package jug.lambda;

import java.io.Serializable;
import java.util.function.Supplier;

public class MultiplePersonalities {
  public Supplier<String> greet() {
    return (Supplier<String> & Serializable) () -> "Hello!";
  }
}
