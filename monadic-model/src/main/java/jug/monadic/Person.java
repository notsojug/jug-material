package jug.monadic;

import org.immutables.value.Value;

import javaslang.control.Validation;

@Value.Immutable(builder = false)
@Value.Style(jdkOnly = true)
public interface Person {

  @Value.Parameter(order = 1)
  String getName();

  @Value.Parameter(order = 2)
  int getAge();

  @Value.Parameter(order = 3)
  String cf();

  public static Validation<String, String> validateName(String name) {
    return name != null && name.matches("[\\s\\p{Alpha}\\p{Punct}]+") ? Validation.valid(name)
        : Validation.invalid("Invalid name: " + name);
  }

  public static Validation<String, Integer> validateAge(int age) {
    return age > 0 && age < 100 ? Validation.valid(age) : Validation.invalid("Invalid age: " + age);
  }

  public static Validation<String, String> validateCf(String cf) {
    return cf != null && cf.matches("[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]")
        ? Validation.valid(cf) : Validation.invalid("Invalid cf: " + cf);
  }
}