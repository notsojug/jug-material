class: center, middle

# Immutables

## An immutable class is simply a class whose instances cannot be modified

They are easier to design, implement, and use, less prone to error and more secure than mutable classes

---

# Five rules to make a class immutable

1. Don’t provide any methods that modify the object’s state

--

2. Ensure that the class can’t be extended
  * The alternative to making an immutable class final is to make all of its constructors private or package-private, and to add public static factories in place of the public constructors

--

3. Make all fields final

--

4. Make all fields private

--

5. Ensure exclusive access to any mutable components

???
Make defensive copies in constructors, accessors, and readObject methods.

---

# Advantages

* **Immutable objects are simple** can be in exactly one state, the state in which it was created
* **Immutable objects are inherently thread-safe** they require no synchronization
* **Immutable objects can be shared freely** encouraging clients to reuse existing instances wherever possible
* **Not only can you share immutable objects, but you can share their internals** the `BigInteger::negate` method produces a new `BigInteger` that points to the same internal array as the original

In truth, no method may produce an _externally visible_ change in the object’s state. However, some immutable classes have one or more nonfinal fields in which they cache the results of expensive computations the first time they are needed.

---

# Disadvantage

* **The only real disadvantage of immutable classes is that they require a separate object for each distinct value** creating these objects can be costly, especially if they are large

The `flipBit` method creates a new `BigInteger` instance, also a million bits long, that differs from the original in only one bit. The operation requires time and space proportional to the size of the `BigInteger`.

Contrast this to `java.util.BitSet`. Like `BigInteger`, `BitSet` represents an arbitrarily long sequence of bits, but unlike `BigInteger`, `BitSet` is mutable. The `BitSet` class provides a method that allows you to change the state of a single bit of a million bit instance in constant time.

---

# Disadvantage, with solution

### Mutable, and private, "companion class"

`BigInteger` has a package-private mutable "companion class" that it uses to speed up multistep operations such as modular exponentiation. Luckily you don’t have to use the mutable companion class: the implementors of `BigInteger` did the hard work for you.

### Public mutable companion class

The main example of this approach in the Java platform libraries is the `String` class, whose mutable companion is `StringBuilder`. Arguably, `BitSet` plays the role of mutable companion to `BigInteger` under certain circumstances.

---
class: center, middle

# So wat?

## Classes should be immutable unless there’s a **very good reason** to make them mutable

_You should provide a **public mutable companion** class for your immutable class only once you’ve confirmed that it’s necessary to achieve satisfactory performance_

_Therefore, **make every field final unless there is a compelling reason to make it nonfinal**_

---
class: center, middle

# **But I'm so lazy...**

--

## Immutables annotation processor to the rescue!

[immutables.github.io](https://immutables.github.io/intro.html)

### Java annotation processors to generate simple, safe and consistent value objects.

---

# Immutables

You can think of it as [Guava's Immutable Collections](https://github.com/google/guava/wiki/ImmutableCollectionsExplained) but for regular objects.

```xml
<dependency>
  <groupId>org.immutables</groupId>
  <artifactId>value</artifactId>
  <version>${immutables.version}</version>
  <scope>provided</scope> <!-- or <optional>true</optional> -->
</dependency>
```
The dependency can be declared in the `provided` scope, or made `optional`.

The artifact is not required at runtime; it is **compile-only dependency**!

---

# Value added

.remark-slide-content-big[* no boilerplate setter and getters
* no ugly IDE-generated `hashCode`, `equals` and `toString` methods that end up being stored in source control
  * hashCode precomputing
* Default attributes, Derived attributes, Lazy attributes, Optional attributes
* Factory builders
* Generate functions and predicates
* JSON serialization
  * JAX-RS integration]

---

# Beauty

Object definitions are pleasant to write and read

# [WOW](https://immutables.github.io/generated.html)

```java
import org.immutables.value.Value;

@Value.Immutable(builder = false, copy = false)
interface Tuple {
  @Value.Parameter int getIndex();
  @Value.Parameter String getName();
}
```

---
class: center, middle

# **What is your excuse now...?**