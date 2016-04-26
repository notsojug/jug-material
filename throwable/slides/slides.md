class: center, middle

# Exceptions

## A _glorified multilevel **goto**_ in Java

(cit. [Mario Fusco](http://www.slideshare.net/mariofusco/from-object-oriented-to-functional-domain-modeling/33))

---

When used to best advantage, exceptions can improve a program’s readability, reliability, and maintainability.

When used improperly, they can have the opposite effect.

```java
// Horrible abuse of exceptions. Don't ever do this!
try {
  int i = 0;
  while(true)
    range[i++].climb();
} catch(ArrayIndexOutOfBoundsException e) {
  // GOTO outta here!
}
```

```java
for (Mountain m : range) // Safer, faster, better!
  m.climb();
```

---

## There are three things wrong with this reasoning

* Because exceptions are designed for **exceptional circumstances**, there is little incentive for JVM implementors to make them as fast as explicit tests.
* Placing code inside a try-catch block **inhibits certain optimizations** that modern JVM implementations might otherwise perform.
* The standard idiom for looping through an array doesn’t necessarily result in redundant checks. Modern JVM implementations optimize them away.

---

### A well-designed API must not force its clients to use exceptions for ordinary control flow

```java
// With hasNext method
for (Iterator<Foo> i = collection.iterator(); i.hasNext(); ) {
  Foo foo = i.next();
  ...
}
```
```java
try { // Without hasNext method
  Iterator<Foo> i = collection.iterator();
  while(true) {
  Foo foo = i.next();
  ...
  }
} catch (NoSuchElementException e) { } // Discarded Exception
```
---

# Use checked exceptions for recoverable conditions and runtime exceptions for programming errors

Each checked exception that a method is declared to throw is therefore a potent indication to the API user that the associated condition is a possible outcome of invoking the method.

The user can disregard the mandate by catching the exception and ignoring it, but this is usually a **bad idea**.

---

## Use checked exceptions for recoverable conditions

Because checked exceptions generally indicate recoverable conditions, it’s especially important for such exceptions to provide methods that furnish information that could help the caller to recover.

.remark-slide-content-medium[`public class Exception extends Throwable`

The class Exception and its subclasses are a form of `Throwable` that indicates conditions that a reasonable application might want to catch.
The class Exception and any subclasses that are not also subclasses of `RuntimeException` are checked exceptions. Checked exceptions need to be declared in a method or constructor's throws clause if they can be thrown by the execution of the method or constructor and propagate outside the method or constructor boundary.]

---

## Use runtime exceptions to indicate programming errors.

The great majority of runtime exceptions indicate precondition violations. A precondition violation is simply a failure by the client of an API to adhere to the contract established by the API specification. 

For example, the contract for array access specifies that the array index must be between zero and the array length minus one.

`ArrayIndexOutOfBoundsException` indicates that this precondition was violated.

---

## Use runtime exceptions to indicate programming errors.

.remark-slide-content-medium[`public class RuntimeException extends Exception`

RuntimeException is the superclass of those exceptions that can be thrown during the normal operation of the Java Virtual Machine.

RuntimeException and its subclasses are _unchecked exceptions_. Unchecked exceptions do not need to be declared in a method or constructor's throws clause if they can be thrown by the execution of the method or constructor and propagate outside the method or constructor boundary.]

Read javadoc for: [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html),
[RuntimeException](https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html),
[Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html) and
[Error](https://docs.oracle.com/javase/8/docs/api/java/lang/Error.html)! This is a **must** not an advice.

---

## It is possible to define a throwable that is not a subclass of Exception, RuntimeException, or Error?

The JLS does not address such throwables directly but specifies implicitly that they are behaviorally identical to ordinary checked exceptions (which are subclasses of Exception but not RuntimeException ).

So when should you use such a beast?

**In a word, never.**

It has no benefits over an ordinary checked exception and would merely serve to confuse the user of your API.

---

## Avoid unnecessary use of checked exceptions

.left-column[from this:

```
// Invocation with checked
// exception
try {
  obj.action(args);
} catch(TheCheckedException e) {
  // Handle exceptional
  // condition
}
```
]
.right-column[to this:

```
// Invocation with state-testing
// method and unchecked exc.
if (obj.actionPermitted(args)) {
  obj.action(args);
} else {
  // Handle exceptional
  // condition
}
```
]

---

## Favor the use of standard exceptions

Reusing preexisting exceptions has several benefits. Chief among these, it makes your API easier to learn and use because it matches established conventions with which programmers are already familiar.

A close second is that programs using your API are easier to read because they aren’t cluttered with unfamiliar exceptions. Last (and least), fewer exception classes mean a smaller memory footprint and less time spent loading classes.
.remark-slide-content-medium[.left-column[
* IllegalArgumentException
* IllegalStateException
* NullPointerException
* IndexOutOfBoundsException].right-column[
* UnsupportedOperationException
* ConcurrentModificationException
* NoSuchElementException
* SecurityException]]

---

## Throw exceptions appropriate to the abstraction

Higher layers should catch lower-level exceptions and, in their place, throw exceptions that can be explained in terms of the higher-level abstraction.

### Exception translation

```
try {
  // Use lower-level abstraction to do our bidding
} catch(LowerLevelException e) {
  throw new HigherLevelException(...);
}
```

---

## Throw exceptions appropriate to the abstraction

### Exception Chaining

```
try {
  // Use lower-level abstraction to do our bidding
} catch (LowerLevelException cause) {
  throw new HigherLevelException(cause);
}
```

**While exception translation is superior to mindless propagation of exceptions from lower layers, it should not be overused.**

---

## Don’t ignore exceptions

An empty catch block defeats the purpose of exceptions.

_Ignoring an exception is analogous to ignoring a fire alarm and turning it off so no one else gets a chance to see if there’s a real fire._

```
// Empty catch block ignores exception - Highly suspect!
try {
  ...
} catch (SomeException e) { }
```

At the very least, the catch block should contain a comment explaining why it is appropriate to ignore the exception.

---

# Why Catching Throwable or Error is bad?

.remark-slide-content-medium[_Javadoc: An Error is a subclass of Throwable that indicates serious problems that a reasonable application should not try to catch. Most such errors are abnormal conditions. The ThreadDeath error, though a "normal" condition, is also a subclass of Error because most applications should not try to catch it._]

Catching some fatal VM errors like `OutOfMemoryError` or `StackOverflowError` is a nonsense. Оn the other hand, if you load a native libraries, you have to deal with `UnsatisfiedLinkError` error.

Nope. **Never ever do it**.

---

# Memento

* Document all exceptions thrown by each method.
* Never declare that a method `throws Exception` or, worse yet, `throws Throwable`.
* The language does not require programmers to declare the unchecked exceptions that a method is capable of throwing, it is wise to document them as carefully as the checked exceptions.
* Include failure-capture information in detail messages.
* A failed method invocation should leave the object in the state that it was in prior to the invocation. A method with this property is said to be _failure atomic_. **There are several ways to achieve this effect. The simplest is to design immutable objects**