class: center, middle

# Monadic Java

## Part II

## Monadic modeling and coding

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

## Links:

* https://www.youtube.com/watch?v=SN_hqTn4N6Y
* https://bruceeckel.github.io/2015/10/17/are-java-8-lambdas-closures/
* http://www.nurkiewicz.com/2016/06/functor-and-monad-examples-in-plain-java.html
* http://nazarii.bardiuk.com/java-monad/
* http://www.javaslang.io/
* https://dzone.com/articles/higher-order-functions

---

class: center, middle

# Questions

