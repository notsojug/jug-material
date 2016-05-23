class: center, middle

# Dependency Injection or DI

> Formerly known as Inversion of Control and the associated _Hollywood Principle_: "Don't call us, we'll call you."

---

# Example

Any nontrivial application is made up of two or more classes that collaborate with each other to perform some business logic.

Traditionally:
* each object is responsible for obtaining its own references to the objects it collaborates with (its dependencies)

When applying DI:
* the objects are given their dependencies at creation time
* dependencies are injected into objects.

---

# Originally called Inversion of Control (IoC)

Because the normal control sequence would be:
* the object finds the objects it depends on by itself and then **calls them**.

Here, this is reversed:
* the dependencies are **handed to the object** when it's created.

This also illustrates the **Hollywood Principle** at work:
> Don't call around for your dependencies, we'll give them to you when we need you.

---

# Wondering why it's a big deal?

## It delivers a key advantage: loose coupling

* objects can be added and tested independently of other objects
* they don't depend on anything other than what you pass them
* it's possible to test the object in isolation passing it mock objects

## When using traditional dependencies

* to test an object you have to create an environment where:
  * all of its dependencies exist
  * are reachable before you can test it

---

# The challenge of DI

## Writing an entire application using it.

A few classes are no big deal, but a whole application is _much more difficult_.

For entire applications, you frequently want a framework to manage the dependencies and the interactions between objects.

* **Guice/Spring**: [JSR 330: Dependency Injection for Java](https://jcp.org/en/jsr/detail?id=330)
* **Weld**: [JSR 299: Contexts and Dependency Injection for the Java EE platform](https://jcp.org/en/jsr/detail?id=299)

---

# Advantages / Disadvantages

* Weld/CDI
  * Standardization: Java EE 6+
  * Container support: Jboss, ...
* Guice/Spring
  * Actual application

## We choose Weld, but it's the principle that matters.

---

# Advantages of DI

* allows a client the flexibility of being configurable
* can be used to externalize a system's configuration details
* it can be applied to legacy code as a refactoring
  * this is often the first benefit noticed when using DI
* allows a client to remove all knowledge of a concrete implementation
  * it promotes reusability, testability and maintainability
  * reduction of boilerplate code (all work to set up dependencies is handled by a DI FW)
* allows concurrent or independent development
* decreases coupling (once again!)

And many more to discover!

---

# Back to SOLID

1. High-level modules should not depend on low-level modules. Both should depend on abstractions.
2. Abstractions should not depend on details. Details should depend on abstractions.

Implications in an Object Oriented program:
* All member variables in a class must be interfaces or abstracts.
* All concrete class packages must connect only through interface or abstract class packages.
* No class should derive from a concrete class.
* No method should override an implemented method.
* All variable instantiation requires the use of a Dependency Injection framework.

---

# What if... Function Programming?

Concepts:
* Higher order functions
* Immutable data
* Pure functions

Let's see **Higher Order Functions**!

The same way you can create and pass around objects in an object-oriented language, in a functional language you can create and pass around functions.

> A function can be created and then assigned to a variable, or passed as an argument of another function.

---

# What if... Function Programming?

A higher order function is one that can accept another function as its argument

This is a very powerful concept which allows for a natural implementation of **inversion of control**, which is one of the most important design principles in software development.

### Immutable data: do you remember?

### Pure Functions: more on that later

**This and more in the next episode!**

---

## Links:

* Hollywood principle [https://en.wikipedia.org/wiki/Inversion_of_control]()
* Article about FP: [https://markonis.github.io/functional/programming/2016/04/16/intro-to-functional-programming.html]()
* Interesting slides: [http://www.slideshare.net/RichardWarburton/twins-object-oriented-programming-and-functional-programming]()
* An article from Martin Fowler about injection [http://martinfowler.com/articles/injection.html]()
* DZone on High order Functions [https://dzone.com/articles/higher-order-functions]()

---

class: center, middle

# Questions
