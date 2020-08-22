class: center, middle

# Monadic Java

## Part II

## Functional programming and VAVR

---

# Functional program

- only pure functions

---
## Pure functions

No (observable) side effects like

1. reassigning a variable
2. modifying a data structure
3. setting a field of an objet
4. throwing an exception
5. print to the console
6. read user input
7. read or write a file
8. draw on the screen

--

1-4 avoidable, 5-8 deferrable

---
## Functional programming

A restriction on **how** to write a program, not on **what** it can do.

Bonus: it's very easy to test it: given the parameters of a function, there is only one result.

--

i.e. you can have a functional, completely verifiable, **core set of functionalities** and use non-pure function to bind them at the ends.

---

# OOP vs FP

* OOP makes code undestandable by **encapsulating** moving parts.
	
* FP makes code understandable by **minimizing** moving parts

Michael Feathers.

--

It's not all black (OOP) or white (FP), there are at least 50 shades of gray in the middle...

---

# Simple example

Let's describe a simple story...

```
public class Bird {}

public class Cat {
	private Bird bird;
	private boolean full;
	public void capture(Bird b) { bird = b; }
	public void eat() {
		full = true;
		bird = null;
	}
}
```
---

# Simple example

The plot:

```
void story() {
	Cat cat = new Cat();
	Bird bird = new Bird();

	cat.capture(bird);
	cat.eat();
}
```

---

# Simple example: with FP

The same story with functional programming: how to describe a cat with a catched bird?

--
With types!

```
public class Bird {}

public class Cat {}

public class CatWithCatch {}

public class FullCat {}
```

--

How to connect the dots?

---

# Simple example: with FP

```
public class Cat {
	public CatWithCatch capture(Bird b) { return new CatWithCatch(b); }
}

public class CatWithCatch {
	private final Bird bird;

	public CatWithCatch(Bird bird) { this.bird = bird; }
	
	public FullCat eat() { return new FullCat(); }
}
```
---

# Simple example: with FP

The plot:

```
void story() {
	BiFunction<Cat, Bird, FullCat> story = 
			((BiFunction<Cat, Bird, CatWithCatch>) Cat::capture)
			.andThen(CatWithCatch::eat);
	
	FullCat fullCat = story.apply(new Cat(), new Bird());
}
```
---

# Simple example: with FP

The plot, better:

```
static <T,U,R> BiFunction<T, U, R> bifunction(
	BiFunction<T, U, R> f) {
	return f;
}

public static void story(){
	BiFunction<Cat, Bird, FullCat> story = 
		bifunction(Cat::capture)
		.andThen(CatWithCatch::eat);
}
```

---

# Simple example: with FP

Observations:

- more expressive use of types
	* a cat without a catch cannot `eat()`
	* each type represents a state
- more verbosity (duh)
- immutability helps enforcing correctness
- can be tested very easily:
	it compiles? it should work
- focus on verbs (functions) rather than nouns (objects)
- function compose better than objects

---

# Enter...VΛVR

	VAVR core is a functional library for Java 8+.
	It helps to reduce the amount of code and to increase
	the robustness. A first step towards functional programming
	is to start thinking in immutable values. Vavr provides
	immutable collections and the necessary functions and
	control structures to operate on these values.

---

# VΛVR

It has:
* Tuples
* Functions goodies
* Immutable Monadic Collections
* Structural Pattern Matching
* A set of useful monads

---

# VΛVR

Tuples:

```
// (Java, 8)
Tuple2<String, Integer> java8 = Tuple.of("Java", 8); 

// "Java"
String s = java8._1; 

// 8
Integer i = java8._2; 
```

---

# VΛVR 

Tuple have (of course, you may say) a `map` function.

```
Tuple2<String, Integer> take = Tuple.of("Va", 8); 
// (Va, 8)
Tuple2<String, Integer> that = take.map(
        s -> s + "vr",
        i -> i / 4
);
// (Vavr, 2)
```

---
#  Function goodies

```
// sum.apply(1, 2) = 3
Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;
```
--
```
Function3<String, String, String, String> function3 =
        Function3.of(this::methodWhichAccepts3Parameters);
```
---
#  Function goodies
And also:

* Composition
* Lifting
* Currying
* Memoization

---

# Lifting

Consider

```
Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
``` 
What happens when `b` is zero? Exceptions!

--
```
Function2<Integer, Integer, Option<Integer>> safeDivide =
    Function2.lift(divide);

// = None
Option<Integer> i1 = safeDivide.apply(1, 0); 

// = Some(2)
Option<Integer> i2 = safeDivide.apply(4, 2); 
```
---
# Monads

Vavr offers some interesting monads

* Option
* Try
* Either
* All vavr collections are monads (O'Rly?)

---
# Option

It's java's `Optional`, but serializable.
--


It is an interface with two implementations: `Some` and `None` for instances with or without value.

It supports the same creation model of java.util.Optional:
```
Option<String> something = Option.of("hello");
// Some(hello)
Option<Integer> nothing = Option.none();
// None
Option<Integer> nothingToo = Option.of(null);
// None
```

---
# Option

Supports map/flatMap and a few other handy features like:
- conversion to VAVR's and java.util's collections, and to plain arrays
- conversion to `java.util.Stream` via `.toJavaStream()`
- it is itself an instance of `java.util.Iterable`

---
# Option - WARNING

Since `Option` respects the monad laws, it supports the creation of a `Some(null)`, for example:
```
Option<String> something = Option.of("hello")
	.map(ignore -> null);
// Some(null)
```

This is done because the purpose of an `Option` is to express the absence or presence of value, and *it respects the monad laws*, while `java.util.Optional` never tries to adhere the monad laws.

---
# Try monad

Wraps the result of a function which may fail (i.e. may throw an exeption).

It can be a `Success(value)` or a `Failure(exception)`.

--

```
Try.of(() -> "hello");
//Success(hello)
Try.of(() -> surelyFailing());
//Failure(java.lang.RuntimeException: hold on)
```

_Try_ also supports `map` and `flatMap` because, of course, is a **monad**

---
# Try monad

More examples:

```
Try.of(() -> bunchOfWorkThatMayThrow())
	.map(this::transformationWithNoExceptions)
	.mapTry(this::anotherPossibleThrowingFunction);
	.getOrElse(defaultValue);
```

`mapTry` behaves like `map` but also accepts functions that throw exceptions.

---
# Try monad

You can also try to recover from the failure. Here is an example using the structural pattern matching from vavr:

```
A result = Try.of(this::bunchOfWork)
  .recover(x -> Match(x).of(
    Case(instanceOf(Exception_1.class), createDefaultA()),
    Case(instanceOf(Exception_2.class), () -> getAFromSpace()),
    Case(instanceOf(Exception_n.class), ex -> transformAFromException(ex))
  ))
  .getOrElse(other);
```

---
# Either monad

	Either represents a value of two possible types.
	An Either is either a Left or a Right.

The `Either` monad is right-biased, which means that the happy path is on the right ("right" also means "good" or "correct" in english), while the errors are on the left. So each `map` and `flatMap` operate only if the value is `Right(something)`.

There are specific alternative methods to operate on the left part, we will see them.

---
# Either monad

How is it used? With `map` and `flatMap` , of course... or with `mapLeft` if you need to transform the `Left` version.

--

Directly, for example in a method:

```
Either<ErrorBean, GoodValue> getGoodValue(int identifier) {
	// bunch of work
	if (weAreGood()) {
		return Either.right(GoodValue.of("well done"));
	}
	return Either.left(ErrorBean.of("something went wrong"));
}
```

---
# Either monad

From a try conversion:

```
Try.of(() -> surelyFailing()).toEither();
// Either<Throwable, String>
```
--

Then you can map the exception to whatever you need:

```
Try.of(() -> surelyFailing())   // Try<String>
  .map(Integer::parseInt)       // Try<Integer>
  .toEither()                   // Either<Throwable, Integer>
  .map(integer->integer * 2)    // Either<Throwable, Integer>
  .mapLeft(throwed ->
     ErrorBean.of(throwed));    // Either<ErrorBean, Integer>
```

---
# Either monad - railway programming

Assuming all computations have a good/right/happy path, and a wrong/left/error path, we can transform the try-compute-catch-throw into an Either series of transformations.

---
## Either monad - railway programming
Compare the following example from a REST endpoint, assuming a proper exception mapper:

```
String result1;
try {
	result1 = bunchOfWorkThatMayThrow();
} catch (Exception ex1) {
	throw ExceptionOnFirstStep(ex1); // error path
}
try {
	Integer result2 = findSomeData(result1); // happy path
	return result2 / 4; // happy path
} catch (NullPointerException ex3) {
	return DEFAULT_VALUE; // default result (error path)
} catch (Exception ex2) {
	throw new ExceptionOnSecondStep(ex2); // error path
}
```

---
## Either monad - railway programming
To the same with Either and proper error mapping:

```
// assuming we modify the other methods
Either<ErrorBean, String> bunchOfWorkThatMayThrow() {}
Option<Integer> findSomeData(String identifier) {}

// then we get
return bunchOfWorkThatMayThrow()
  .map(identifier -> findSomeData(identifier)
    .map(result -> result / 4)
    .orElse(DEFAULT_VALUE))
  .mapLeft(errorBean -> toJson(errorBean))
```
---
## Either monad - railway programming
To the same with Either and proper error mapping:

```
// assuming we modify the other methods
Either<ErrorBean, String> bunchOfWorkThatMayThrow() {}
Option<Integer> findSomeData(String identifier) {}

// then we get
return bunchOfWorkThatMayThrow()  // happy path
  .map(identifier -> findSomeData(identifier) // happy path
    .map(result -> result / 4) // happy path
    .orElse(DEFAULT_VALUE)) // default result (error path)
  .mapLeft(errorBean -> toJson(errorBean)) // error path
```
---
## Either monad - railway programming
To the same with Either and proper error mapping:

```
// assuming we modify the other methods
Either<ErrorBean, String> bunchOfWorkThatMayThrow() {}
Option<Integer> findSomeData(String identifier) {}

// then we get
return bunchOfWorkThatMayThrow()  // Either<ErrorBean, String>
  .map(identifier -> findSomeData(identifier) // Option<Integer>
    .map(result -> result / 4) // Option<Integer>
    .orElse(DEFAULT_VALUE)) // Either<ErrorBean, Integer>
  .mapLeft(errorBean -> toJson(errorBean))
```
--
That is, if we model it correctly, we can see and think about the happy path clearly, and deal with the errors separately.
---
# Other

* immutable collections which are also monads, see examples on next slides
* a proper `Stream` class
* the `Validation` applicative functor (covered in the test code)

---
# List monad

Vavr's `List` (and each datastructure in the library) is an immutable data structure which is also a monad. 

I.e. it has `map` and `flatMap` built-in, and follows the monad laws.

--

So why streams in jdk? 

---
Now we have the functional superpowers, we can modify our models...


---

## Links:

* http://www.slideshare.net/mariofusco/from-object-oriented-to-functional-domain-modeling
* http://www.vavr.io/
* https://www.vavr.io/vavr-docs/#_pattern_matching

---

class: center, middle

# Questions

