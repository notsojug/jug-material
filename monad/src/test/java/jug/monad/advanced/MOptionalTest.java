package jug.monad.advanced;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MOptionalTest {
  
  @Test
  public void correctMonadTest() throws Exception {
    MOptional<String> str = MOptional.of("42");
    MOptional<?> num = str.flatMap(this::tryParse);
    assertThat(num).isInstanceOf(MOptional.class);
  }
  
  MOptional<Integer> tryParse(String s) {
    try {
      final int i = Integer.parseInt(s);
      return MOptional.of(i);
    } catch (NumberFormatException e) {
      return MOptional.empty();
    }
  }

}
