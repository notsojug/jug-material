class: center, middle

# Optional

## A better alternative to null
Raise your hand if you ever got a NullPointerException during your life as a Java developer.

---

# The inventor

.center[![Billion-Dollar](http://www.azquotes.com/picture-quotes/quote-i-call-it-my-billion-dollar-mistake-it-was-the-invention-of-the-null-reference-in-1965-tony-hoare-113-96-46.jpg)]

---

# How do you model the absence of a value?

```
public class Person {
	private Car car;
	public Car getCar() { return car; }
}
public class Car {
	private Insurance insurance;
	public Insurance getInsurance() { return insurance; }
}
public class Insurance {
	private String name;
	public String getName() { return name; }
}
```
---

### Then, what’s possibly problematic with the following code?

```
public String getCarInsuranceName(Person person) {
	return person.getCar().getInsurance().getName();
}
```

_Will it blend?_

```
public String getCarInsuranceName(Person person) {
	if (person != null && person.getCar() != null &&
			person.getCar().getInsurance() != null)
		return person.getCar().getInsurance().getName();
	else
		return "Unknow Name";
}
```

---

# So what?

* **A source of error.** NullPointerException is by far the most common exception in Java.
* **It bloats your code.** Deeply nested null checks.
* **It’s meaningless.** It doesn’t have any semantic meaning.
* **It breaks Java philosophy.** Java always hides pointers from developers except in one case: _the null pointer_.
* **It creates a hole in the type system.** null carries no type or other information, meaning it can be assigned to any reference type.

_And in particular it represents the wrong way to model the absence of a value in a statically typed language._

---

## Introducing the Optional class

Java 8 introduces a new class called `java.util.Optional<T>` that’s inspired by the ideas of Haskell and Scala.

It’s a class that encapsulates an optional value.

When a value is present, the Optional class just wraps it.
Conversely, the absence of a value is modeled with an “empty” optional returned by the method `Optional.empty()`.

You might wonder what the difference is between a null reference and `Optional.empty()` which is a valid, workable object of type Optional that can be invoked in useful ways.

???
Absent is a static factory method that returns a special singleton instance of the Optional class.

---

## Introducing the Optional class, for Java 6

### Yes! Guava has `com.google.common.base.Optional<T>` too!

Unlike the Java one this implements _Serializable_!

Has similar methods but with different names:
* `fromNullable` and `ofNullable`
* `absent` and `empty`
* `or` and `orElse`
* `transform` and `map`/`flatMap`

But sometimes not: `get`, `isPresent`.

???
This can be used like a field.

---

## Example: Guava

```
public class Insurance {
    private String name;
    public String getName() { return name; }
}

public String getInsuranceName(Insurance insurance) {
	return Optional.fromNullable(insurance)
		.transform(new Function<Insurance, String>() {
			@Override public String apply(Insurance input) {
				return input.getName();
			}
    }).or("Unknow");
}
```

Java 6 style! Still ugly...

---

## Example: Method returns Optional&lt;T&gt;

Returning Optional adds explicitly the possibility that there might not be a name for that given Insurance.

This means that the caller of the method is explicitly forced by the type system to think about and deal with the possibility that there might not be a name with that Insurance.

```
public class Insurance {
    private String name;
    public Optional<String> getName() {
    	return Optional.fromNullable(name);
    }
}
```

---

## Example: Java 8

```
public class Insurance {
	private String name;
	public Optional<String> getName() {
		return Optional.ofNullable(name);
	}
}
```
```
insurance.getName().orElse("Unknown");
```

Bye Bye NullPointerException!

---

## Interesting methods

### Guava

* `Set<T> asSet()`: immutable singleton Set if it is present; an empty immutable Set otherwise.
* `T or(T)`: contained instance if it is present; defaultValue otherwise.
* `Optional<V> transform(Function<? super T, V>)`: if the instance is present, it is transformed with the given Function; otherwise, Optional.absent is returned.

---

## Interesting methods

### Java

* `Optional<T> filter(Predicate<? super T>)`: when predicate is false return an empty optional
* `Optional<U> map(Function<? super T, ? extends U>)`: if a value is present, apply the provided mapping function to it
* `Optional<U> flatMap(Function<? super T, Optional<U>>)`: like map, but the function result in another optional
* `void ifPresent(Consumer<? super T>)`: if a value is present, invoke the specified consumer with the value
* `T orElse(T)`
* `<X extends Throwable> T orElseThrow(Supplier<? extends X>)`

---

# What is Optional not trying to solve

Optional is not meant to be a mechanism to avoid all types of null pointers. The mandatory input parameters of methods and constructors still have to be tested for example.

## What is wrong with just returning null?

The problem is that **the caller of the function might not have read the javadoc** for the method, and forget about handling the null case.

This happens frequently and is one of the main causes of null pointer exceptions, although not the only one.

---

# How should Optional NOT be used?

Optional is not meant to be used in these contexts:

* in the domain model layer (not serializable)
* in DTOs (same reason)
* in input parameters of methods
* in constructor parameters

## Links:

* [Oracle blog](http://www.oracle.com/technetwork/articles/java/java8-optional-2175753.html)
* [Guava wiki](https://github.com/google/guava/wiki/UsingAndAvoidingNullExplained)