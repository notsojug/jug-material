package jug.monadic.slang;

import static jug.monadic.Person.validateAge;
import static jug.monadic.Person.validateCf;
import static jug.monadic.Person.validateName;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.Test;

import io.vavr.control.Validation;
import jug.monadic.ImmutablePerson;
import jug.monadic.Person;
import jug.monadic.ValidationException;

public class ValidationTest {

  static final String W_NAME = "Cesar 123";
  static final String C_NAME = "Mary Stuard";
  static final Integer W_AGE = -5;
  static final Integer C_AGE = 37;
  static final String W_CF = "QWERTY80311A123B";
  static final String C_CF = "RSSMRA80A01H501U";
  static final Condition<String> WRONG_NAME = new Condition<String>((s -> s.contains(W_NAME)),
      "Contains wrong name");
  static final Condition<String> WRONG_AGE = new Condition<String>(
      (s -> s.contains(W_AGE.toString())), "Contains wrong age");
  static final Condition<String> WRONG_CF = new Condition<String>(
      (s -> s.contains(W_CF.toString())), "Contains wrong CF");

  @Test
  public void shouldValidate() throws Exception {
    Validation<ValidationException, Person> validation = Validation
        .combine(validateName(C_NAME), validateAge(C_AGE), validateCf(C_CF))
        .<Person> ap(ImmutablePerson::of).mapError(ValidationException::of);

    validation.peek(System.out::println); validation.swap().peek(System.out::println);

    assertThat(validation.isValid()).isTrue();
    assertThat(validation.map(Person::getName).toJavaOptional()).isPresent().contains(C_NAME);
    assertThat(validation.toJavaOptional().map(Person::getName)).isPresent().contains(C_NAME);
  }

  @Test
  public void shouldNotValidateName() throws Exception {
    Validation<ValidationException, Person> validation = Validation
        .combine(validateName(W_NAME), validateAge(C_AGE), validateCf(C_CF))
        .<Person> ap(ImmutablePerson::of).mapError(ValidationException::of);

    validation.peek(System.out::println); validation.swap().peek(System.out::println);

    assertThat(validation.isInvalid()).isTrue();
    assertThat(validation.map(Person::getName).toJavaOptional()).isNotPresent();
    assertThat(validation.toJavaOptional().map(Person::getName)).isNotPresent();
    ValidationException errors = validation.getError();
    assertThat(errors.getErrors()).hasSize(1).areAtLeastOne(WRONG_NAME).areNot(WRONG_AGE)
        .areNot(WRONG_CF);
  }

  @Test
  public void shouldNotValidateNameAge() throws Exception {
    Validation<ValidationException, Person> validation = Validation
        .combine(validateName(W_NAME), validateAge(W_AGE), validateCf(C_CF))
        .<Person> ap(ImmutablePerson::of).mapError(ValidationException::of);

    validation.peek(System.out::println); validation.swap().peek(System.out::println);

    assertThat(validation.isInvalid()).isTrue();
    assertThat(validation.map(Person::getName).toJavaOptional()).isNotPresent();
    assertThat(validation.toJavaOptional().map(Person::getName)).isNotPresent();
    ValidationException errors = validation.getError();
    assertThat(errors.getErrors()).hasSize(2).areAtLeastOne(WRONG_NAME).areAtLeastOne(WRONG_AGE)
        .areNot(WRONG_CF);
  }

  @Test
  public void shouldNotValidateNameAgeCf() throws Exception {
    Validation<ValidationException, Person> validation = Validation
        .combine(validateName(W_NAME), validateAge(W_AGE), validateCf(W_CF))
        .<Person> ap(ImmutablePerson::of).mapError(ValidationException::of);

    validation.peek(System.out::println); validation.swap().peek(System.out::println);

    assertThat(validation.isInvalid()).isTrue();
    assertThat(validation.map(Person::getName).toJavaOptional()).isNotPresent();
    assertThat(validation.toJavaOptional().map(Person::getName)).isNotPresent();
    ValidationException errors = validation.getError();
    assertThat(errors.getErrors()).hasSize(3).areAtLeastOne(WRONG_NAME).areAtLeastOne(WRONG_AGE)
        .areAtLeastOne(WRONG_CF);
  }

}
