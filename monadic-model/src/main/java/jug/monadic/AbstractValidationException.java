package jug.monadic;

import java.util.Arrays;
import java.util.List;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

@Serial.Structural
@Value.Immutable(builder = false)
@Value.Style(typeAbstract = "Abstract*", typeImmutable = "*", jdkOnly = true)
public abstract class AbstractValidationException extends Exception {

  private static final long serialVersionUID = 238932467334249901L;

  @Value.Parameter
  public abstract List<String> getErrors();

  public static ValidationException of(String... errors) {
    return ValidationException.of(Arrays.asList(errors));
  }

  @Override
  public abstract String toString();

}
