class: center, middle

# Lambda expressions in Java 8

Passing functions to functions

---

# Functional interface

Let's consider a simple interface:

```
public interface Predicate<T> {

	/**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input 
     *			argument matches the predicate,
     * otherwise {@code false}
     */
    boolean test(T t);
}

```

---
# Functional interface

And a simple method using it:

```
private static <T> List<T> filterList(List<T> original, 
		Predicate<T> filter) {
	List<T> result = Lists.newArrayList();
	for(T one : original) {
		if(filter.test(one)) {
			result.add(one);
		}
	}
	return result;
}
```

--

How do we use this interface?

???

And by the way... how this paradigm is called? Strategy pattern? Command pattern? Inversion of control?

---

# Functional interface

```
List<Integer> integers = Lists.newArrayList(1, 5, 7, 8, 10);
Predicate<Integer> filter = new Predicate<Integer>() {
	@Override
	public boolean test(Integer t) {
		return t.intValue() % 2 == 0;
	}
};
List<Integer> filtered = filterList(integers, filter);
``` 

--

Boring, uh?

???

Moreover: this interface has only one method...


---

# Functional interface

What if we could omit the fact that we are implementing a class?

```
List<Integer> integers = Lists.newArrayList(1, 5, 7, 8, 10);
Predicate<Integer> filter = (Integer t) -> {
	return t.intValue() % 2 == 0;
};
List<Integer> filtered = filterList(integers, filter);
```

--

Better, uh?


???

But the body has only one statement, can we do better?

---

# Functional interface

What if we could omit the syntactic of a function?

```
List<Integer> integers = Lists.newArrayList(1, 5, 7, 8, 10);
Predicate<Integer> filter = (Integer t) -> t.intValue() % 2 == 0;
List<Integer> filtered = filterList(integers, filter);
```

--

But `Integer` is already specified in the variable declaration...

???

But... the type of the argument is implicit...can we do even better?


---

# Functional interface

What if we could omit the argument types? 

```
List<Integer> integers = Lists.newArrayList(1, 5, 7, 8, 10);
Predicate<Integer> filter = t -> t.intValue() % 2 == 0;
List<Integer> filtered = filterList(integers, filter);
```

--

Or


```
List<Integer> integers = Lists.newArrayList(1, 5, 7, 8, 10);
List<Integer> filtered = filterList(integers, 
		t -> t.intValue() % 2 == 0);
```

---

### What if I lied to you?: real `Predicate` interface:

```
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
    
    default Predicate<T> and(Predicate<? super T> other) {
        return (t) -> test(t) && other.test(t);
    }

    default Predicate<T> negate() { return (t) -> !test(t); }

    default Predicate<T> or(Predicate<? super T> other) {/* ... */}

    static <T> Predicate<T> isEqual(Object targetRef) {/* ... */}
}
```

--

The hell?

---

### Lambda: what else can I implement as a lambda?

Any interface that is either @FunctionaInterface, or that has a single non-default, non-static method.

Compiler will stop you from doing the wrong thing.

---

### Lambda: ready made lambda-ready interfaces (a selection)

* `java.util.function.*` package
	+ Predicate T -> boolean
	+ Function T -> U
	+ Supplier () -> T
	+ BiFunction (T, U) -> R
	+ ...
* `Runnable` (from java 8)

--

You can define one too!


---

## Method Reference

Consider the following:

```
public static <T, U> List<U> transformList(List<T> original, 
		Function<T, U> function) {
	List<U> result = Lists.newArrayList();
	for (T one : original) {
		result.add(function.apply(one));
	}
	return result;
}
```

---

## Method Reference

What happens if I already have a method with the right signature?

```
class MethodReferenceTest{
	static int multiplyByTwo(int t) { return t * 2; }
}
	
import static jug.lambda.ListUtils.transformList;
void someOtherMethod() {
	List<Integer> integers = Lists.newArrayList(1, 5, 7, 8, 10);
	Function<Integer, Integer> function = 
				MethodReferenceTest::multiplyByTwo;
	List<Integer> tranformed = transformList(integers, function);
}
```

---

## Method Reference

```
import static jug.lambda.ListUtils.transformList;
void someOtherMethod() {
	List<Integer> integers = Lists.newArrayList(1, 5, 7, 8, 10);
	List<String> transformed = transformList(integers, 
		Object::toString);
}
```

---

## Method Reference

Included, but not limited to, instance methods

```
void shouldProduceAUselessList() {
	List<Integer> integers = Lists.newArrayList(1, 5, 7, 8, 10);
	Function<Integer, Boolean> function = integers::contains;
	List<Boolean> transformList = 
		ListUtils.transformList(integers, function);
}
```

---

## Capturing lambdas, and *effectively final*

```
void someMethod(){
	int n = 2;
	Function<Integer, Integer> function = i -> i * n;
	// cannot modify n after the lambda. 
	List<Integer> transformed = 
		ListUtils.transformList(integers, function);
		
}
```

---

# What about SOLID?

* Inversion of Control?

--

	Done

* Interface segregation principle?

	[...] no client should be forced to depend on methods it does not use. ISP splits interfaces that are very large into smaller and more specific ones so that clients will only have to know about the methods that are of interest to them. (Wikipedia)

---

# So?

You are passing **behavior** as a parameter, instead of objects.

--

This is a step towards functional programming
* you can pass objects to other function	
* you can pass functions to other function	
* you can return functions from other functions

---

# Caveats

* Lambdas in `java.util.function.*` do not throw exceptions (we will see why)
* Do not modify state in lambdas, unless you know what you are doing
* Lambdas are not serializable...in general

---

## Links:

* Interface segregation principle ([Wikipedia](https://en.wikipedia.org/wiki/Interface_segregation_principle))
* Default methods ([Oracle tutorial](https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html))
* Java 8 introduction ([Mario Fusco](http://www.slideshare.net/mariofusco/java-8-workshop))
* java.util.function package ([Oracle](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html))
* Tranforming code to java 8 ([Venkat Subramaniam, 1h30](https://www.youtube.com/watch?v=wk3WLaR2V2U))
* Serializing lambdas [http://vanillajava.blogspot.it/2015/07/how-and-why-to-serialialize-lambdas.html]()
* Internals: decompilation of a lambda ([Zeroturnaround](http://zeroturnaround.com/rebellabs/java-8-the-first-taste-of-lambdas/))
* Internals: a brief study on how lambda are implemented ([Brian Goetz](http://cr.openjdk.java.net/~briangoetz/lambda/lambda-translation.html))

---

class: center, middle

# Questions
