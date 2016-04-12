package jug.guava;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class ObjectsTest {
  
  private State wisconsin = new State("WI", "Wisconsin", "MDW", 5726398);
  private State florida = new State("FL", "Florida", "SE", 19317568);
  
  @Test
  public void shouldSameHashCode() {
    assertThat(wisconsin.hashCodeGuava()).isEqualTo(wisconsin.hashCodeGuava());
    
    assertThat(wisconsin.hashCode()).isEqualTo(wisconsin.hashCode());
  }
  
  @Test
  public void shouldDifferentHashCode() {
    assertThat(wisconsin.hashCode()).isNotEqualTo(wisconsin.hashCodeGuava());
    
    assertThat(wisconsin.hashCodeGuava()).isNotEqualTo(florida.hashCodeGuava());
    
    assertThat(wisconsin.hashCode()).isNotEqualTo(florida.hashCode());
  }
  
  @Test
  public void shouldBeEquals() {
    assertThat(wisconsin.equals(wisconsin)).isTrue();
    
    assertThat(florida.equals(florida)).isTrue();
    
    assertThat(wisconsin.equalsGuava(wisconsin)).isTrue();
    
    assertThat(florida.equalsGuava(florida)).isTrue();
  }
  
  @Test
  public void shouldNotEquals() {
    assertThat(wisconsin.equals(florida)).isFalse();
    
    assertThat(florida.equals(wisconsin)).isFalse();
    
    assertThat(wisconsin.equalsGuava(florida)).isFalse();
    
    assertThat(florida.equalsGuava(wisconsin)).isFalse();
  }
  
  @Test
  public void shouldToString_eclipse() {
    assertToString(wisconsin, wisconsin.toString());
  }
  
  @Test
  public void shouldToString_guava() {
    assertToString(wisconsin, wisconsin.toStringGuava());
  }
  
  private static void assertToString(State state, String toString) {
    assertThat(toString)
    .contains(state.getClass().getSimpleName())
    .contains(state.getName(), state.getStateCode())
    .doesNotContain(state.getRegionCode())
    .doesNotContain(String.valueOf(state.getPopulation()));
  }
  
}
