package jug.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.function.Supplier;

import org.junit.Test;

public class IntersectionTypesTest {
  @Test
  public void shouldHaveMultipleInterfaces() {
    Supplier<String> greet = new MultiplePersonalities().greet();
    assertThat(greet.get()).isEqualTo("Hello!");
    assertThat(greet.getClass().getInterfaces()).contains(Supplier.class)
        .contains(Serializable.class);
  }
}
