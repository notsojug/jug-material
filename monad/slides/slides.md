class: center, middle

# Monadic Java

## Part I

## Functor and Monad examples

---

# Back to the future: Functional Programming

First programming language with functional features?
--
 Lisp (late `1950s`)


--
Then
* `1960s` APL

--
* `1970s` FP, ML, NPL

--
* `1980s`/`1990s` **Haskell**

--
* `2000s` *Scala*

--

It is possible to use a functional style of programming in languages that are not traditionally considered functional languages?
--
 **Yes!**

???
What is FP will be studied in deep in Part II

---

# Back to the future: Java 8

## [Lambdas](https://github.com/notsojug/jug-material/tree/master/lambda)/Closures are there to aid functional programming.

Java 8 is not suddenly a functional programming language, but (_like Python_) has some support for functional programming.

## Today's topic: Functors and Monads

--

Wat? [Wiki: Functor](https://en.wikipedia.org/wiki/Functor), [Wiki: Monoid](https://en.wikipedia.org/wiki/Monoid), [Wiki: Monad](https://en.wikipedia.org/wiki/Monad%5F%28category%5Ftheory%29)

---

# Functors

A typed data structure that encapsulates some value(s)

```java
import java.util.function.Function;

interface Functor<T> {
     
    <R> Functor<R> map(Function<T, R> f);
     
}
```

The only operation that functor provides is `map()` that takes a function `f`.

???
`Functor<T>` is always an immutable container, thus map never mutates original object it was executed on.

---

# Functors

The function `map()` receives whatever is inside a box (`T`), transforms it and wraps the result as-is (`R`) into a second box.

When _identity function_ is applied `map(x -> x)` functors should not perform any actions.

**The only way of interacting with the value T is by transforming it**.

--

### Let's go to the _Identity_ functor!

---

# Functors: Identity

```java
interface Functor<T,F extends Functor<?,?>> {
    <R> F map(Function<T,R> f);
}
 
class Identity<T> implements Functor<T,Identity<?>> {
 
    private final T value;
 
    Identity(T value) { this.value = value; }
 
    public <R> Identity<R> map(Function<T,R> f) {
        final R result = f.apply(value);
        return new Identity<>(result);
    }
}
```

---

# Functors: Identity

The only way to interact with functor is by applying sequences of type-safe transformations:

```java
Identity<String> idString = new Identity<>("abc");
Identity<Integer> idInt = idString.map(String::length);
```

```java
Identity<byte[]> idBytes = new Identity<>(customer)
        .map(Customer::getAddress)
        .map(Address::street)
        .map((String s) -> s.substring(0, 3))
        .map(String::toLowerCase)
        .map(String::getBytes);
```

???
What if something is `null`?

---

# Functors: Optional

```java
class FOptional<T> implements Functor<T, FOptional<?>> {

  private final T valueOrNull;

  private FOptional(T valueOrNull) {
    this.valueOrNull = valueOrNull;
  }

  public <R> FOptional<R> map(Function<T, R> f) {
    if (valueOrNull == null) return empty();
    else return of(f.apply(valueOrNull));
  }
```

---

# Functors: Optional

```java
  public static <T> FOptional<T> of(T a) {
    return new FOptional<T>(a);
  }
  public static <T> FOptional<T> empty() {
    return new FOptional<T>(null);
  }
}
```

What differs `FOptional` is that the transformation function `f` may not be applied to any value if it is empty.

It can just as well wrap arbitrary number of values, just like...

---

# Functors: List

```java
class FList<T> implements Functor<T, FList<?>> {
  
  private final ImmutableList<T> list;

  FList(Iterable<T> value) {
      this.list = ImmutableList.copyOf(value);
  }

  @Override public <R> FList<?> map(Function<T, R> f) {
      ArrayList<R> result = new ArrayList<R>(list.size());
      for (T t : list) result.add(f.apply(t));
      return new FList<>(result);
  }
}
```

---

# Functors: List

you have a list of `customers` and you want a list of their streets, it's as simple as:

```java
FList<Customer> customers = new FList<>(asList(cust1, cust2));
 
FList<String> streets = customers
        .map(Customer::getAddress)
        .map(Address::street);
```

you can't invoke `getAddress()` on a collection of `customer`s, you must invoke `getAddress()` on each individual customer and then place it back in a collection. 

---

class: center, middle

# Does this ring a bell?

--

## What if I told you _java.util.stream.Stream&lt;T&gt;_ in Java is a Functor as well?

--

### And by the way, also a Monad?

---

# The future is now

&nbsp;

.center[<img src="The_Future_Is_Now.png" alt="BTTF" width="90%">]

---

# Last example: _Promise_ functor

* "promises" that a value will become available
  * maybe because some background computation was spawned
  * maybe we are waiting for external event
* it will appear some time in the future

--

.left-column[```java
Promise<Customer> customer = //...
Promise<byte[]> bytes = customer
     .map(Customer::getAddress)
     .map(Address::street)
     .map((String s) -> s.substring(0, 3))
     .map(String::toLowerCase)
     .map(String::getBytes);
```]
.right-column[`Promise<Customer`> does not hold a value of Customer just yet. It promises to have such value in the future.

But we can still map over such functor, just like we did with `FOptional`!!]

???
Invoking `customer.map(Customer::getAddress)` yields `Promise<Address>` which means map is non-blocking. `customer.map() `will not wait for the underlying customer promise to complete. **Instead it returns another promise, of different type**.

---

class: center, middle

# Benefits of functors

## **abstract** away the internal representation
## provide **consistent, easy to use** API over data structures

---

# From Functors to Monads

What happens if your transformation function returns functor instance rather than simple value?

```java
FOptional<Integer> tryParse(String s) {
    try {
        final int i = Integer.parseInt(s);
        return FOptional.of(i);
    } catch (NumberFormatException e) {
        return FOptional.empty();
    }
}
```

Let's use it...

---

# From Functors to Monads

```java
FOptional<String> str = FOptional.of("42");
FOptional<FOptional<Integer>> num = str.map(this::tryParse);
```

Apart from looking horrible, having a functor in functor ruins composition and fluent chaining:

```java
FOptional<Integer> num1 = //...
FOptional<FOptional<Integer>> num2 = //...
 
FOptional<Date> date1 = num1.map(t -> new Date(t));
 
//doesn't compile!
FOptional<Date> date2 = num2.map(t -> new Date(t));
```

???
We broke our functor by double wrapping it.

---

# From Functors to Monads

So... Special method named `flatMap()` was introduced.

`flatMap()` is very similar to `map()` but expects the function received as an argument to return a functor - or _monad_ to be precise:

```java
interface Monad<T,M extends Monad<?,?>> extends Functor<T,M> {

    M flatMap(Function<T,M> f);

}
```

`flatMap` (often called `bind` or `>>=` from _Haskell_) allows complex transformations to be composed in a pure, functional style.

???
`flatMap` is not just a syntactic sugar to allow better composition

---

# From Functors to Monads

If `FOptional` was an instance of monad, parsing suddenly **works as expected**:

```java
FOptional<String> num = FOptional.of("42");
FOptional<Integer> answer = num.flatMap(this::tryParse);
```

##  Monads shine when their internal structure is not trivial.

--

Next slide can be disconcerting... Let's skip it.

---

## Monad laws:

Let `f,g` be functions from type of _value_ to M

**Left identity**: `M.of(value).flatMap(f)` equals `f.apply(value)`

**Right identity**: `M.of(value).flatMap(v -> M.of(v))` equals `M.of(value)`

**Associativity**

`M.of(value).flatMap(f).flatMap(g)`

equals

`M.of(value).flatMap(v -> f.apply(v).flatMap(g))`

---

## flatMap as a filter

Another useful operator that we can easily build on top of `flatMap()` is `filter(Predicate<T>)`.

`filter` takes whatever is inside a monad and discards it entirely if it does not meet certain predicate.

In a way it is similar to map but rather than `1-to-1` mapping we have `1-to-0-or-1`.

Obviously it allows filtering out certain elements from a list:

```java
FList<Customer> vips = customers.filter(c -> c.totalOrders > 1000);
```

---

# Very popular and useful FP library for Java: JΛVΛSLΛNG

If you like it we can do a talk about that.

.left-column3[
It has:
* Tuples
* Functions
* Collections
* Pattern Matching
* **Monads**
].right-column7[
```java
A result = Try.of(this::bunchOfWork)
    .recover(x -> {
        LOGGER.error(x);
        return failureResult;
    })
    .getOrElse(otherResult);
```
]

---

## Links:

* https://bruceeckel.github.io/2015/10/17/are-java-8-lambdas-closures/
* http://www.nurkiewicz.com/2016/06/functor-and-monad-examples-in-plain-java.html
* http://nazarii.bardiuk.com/java-monad/
* http://www.vavr.io/
* https://dzone.com/articles/higher-order-functions

---

class: center, middle

# Questions

