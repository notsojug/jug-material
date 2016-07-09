package jug.monad.advanced;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class FOptionalTest {
  
  @Test
  public void wrongFunctorTest() throws Exception {
    FOptional<String> str = FOptional.of("42");
    FOptional<FOptional<Integer>> num = str.map(this::tryParse);
    assertThat(num).isInstanceOf(FOptional.class);
  }
  
  FOptional<Integer> tryParse(String s) {
    try {
      final int i = Integer.parseInt(s);
      return FOptional.of(i);
    } catch (NumberFormatException e) {
      return FOptional.empty();
    }
  }

}
