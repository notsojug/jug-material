package jug.lambda;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import javaslang.Function0;
import javaslang.λ;

public class IntersectionTypesTest {
  @Test
  public void shouldHaveMultipleInterfaces() {
    Function0<String> greet = new MultiplePersonalities().greet();
    assertThat(greet.apply()).isEqualTo("Hello!");
    assertThat(greet.getClass().getInterfaces()).contains(Function0.class)
        .contains(λ.Memoized.class);
  }
}
