package jug.lambda;

import javaslang.Function0;
import javaslang.λ;

public class MultiplePersonalities {
  public Function0<String> greet() {
    return (Function0<String> & λ.Memoized) () -> "Hello!";
  }
}
