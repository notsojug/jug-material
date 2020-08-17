class: center, middle

# Exceptions

## A _glorified multilevel **goto**_ in Java

(cit. [Mario Fusco](http://www.slideshare.net/mariofusco/from-object-oriented-to-functional-domain-modeling/33))

---

# What Is an Exception?

> An exception is an event, which occurs during the execution of a program, that disrupts the normal flow of the program's instructions.

In Java, when an error occurs within a method, the method creates an object and hands it off to the runtime system: the _exception object_.

### What does an _exception object_ contains?

Its type and the state of the program when the error occurred.

Creating an exception object and handing it to the runtime system is called _throwing an exception_.

---

# What Is an Exception?

### What happens when an exception is thrown?

The runtime system searches the call stack for a method that contains a block of code that can handle the exception.

Regardless of what throws the exception, it's always thrown with the **`throw`** statement.

All the exception classes are descendants of the Throwable class.

---

# Throwable Class and Its Subclasses

As you can see, `Throwable` has two direct descendants: `Error` and `Exception`.

<img src="exceptions-throwable.gif" alt="exceptions-throwable" style="width: 346px; display: block; margin-left: auto; margin-right: auto;"/>

**Error Class:** the Java virtual machine throws an `Error`. Simple programs typically do not catch or throw `Errors`.

---

# Throwable Class and Its Subclasses

### Exception Class

An `Exception` indicates that a problem occurred, but it is not a serious system problem.

One `Exception` subclass, `RuntimeException`, is reserved for exceptions that indicate incorrect use of an API.

An example of a runtime exception is `NullPointerException`, which occurs when a method tries to access a member of an object through a `null` reference.

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

1. The standard idiom for looping through an array doesn’t necessarily result in redundant checks. Modern JVM implementations optimize them away.
2. Because exceptions are designed for **exceptional circumstances**, there is little incentive for JVM implementors to make them as fast as explicit tests.
3. Placing code inside a try-catch block **inhibits certain optimizations** that modern JVM implementations might otherwise perform.

* [https://shipilev.net/blog/2014/exceptional-performance/](https://shipilev.net/blog/2014/exceptional-performance/)
* [http://blog.me4502.com/exceptions-as-flow-control-in-java.html](http://blog.me4502.com/exceptions-as-flow-control-in-java.html)

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

# Types of Exceptions

* **checked exceptions** for recoverable conditions
* **runtime exceptions** for programming errors

checked exception that a method is **declared**

therefore a potent indication to the API user that the associated condition is a possible **outcome of invoking the method**.

The user can _disregard_ the mandate by catching the exception and ignoring it, but this is usually a **bad idea**.

---

## Use checked exceptions for recoverable conditions

Checked exceptions generally indicate recoverable conditions

it’s especially important for such exceptions to provide methods that furnish information that could help the caller to recover

Checked exceptions are subject to the Catch or Specify Requirement.

All exceptions are checked exceptions, except for those indicated by `Error`, `RuntimeException`, and their subclasses.

???
`public class Exception extends Throwable`

The class Exception and its subclasses are a form of `Throwable` that indicates conditions that a reasonable application might want to catch.
The class Exception and any subclasses that are not also subclasses of `RuntimeException` are checked exceptions. Checked exceptions need to be declared in a method or constructor's throws clause if they can be thrown by the execution of the method or constructor and propagate outside the method or constructor boundary.

---

## Use runtime exceptions to indicate programming errors

Great majority of runtime exceptions indicate precondition violations.

A precondition violation is simply a failure by the client of an API to adhere to the contract established by the API specification.

For example, the contract for array access specifies that the array index must be between zero and the array length minus one.

`ArrayIndexOutOfBoundsException` indicates that this precondition was violated.

???
`public class RuntimeException extends Exception`

RuntimeException is the superclass of those exceptions that can be thrown during the normal operation of the Java Virtual Machine.

RuntimeException and its subclasses are _unchecked exceptions_. Unchecked exceptions do not need to be declared in a method or constructor's throws clause if they can be thrown by the execution of the method or constructor and propagate outside the method or constructor boundary.

---

class: center, middle

## Here's the bottom line guideline

If a client can reasonably be expected to recover from an exception, make it a checked exception. If a client cannot do anything to recover from the exception, make it an unchecked exception.

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

---

# But why it's so called a _glorified multilevel **goto**_?

```java
public static void main(String[] args) throws Exception {
    try {
      throw new Exception();
    } catch (Exception e) {
      System.out.print("Caught!");
    } finally {
      System.out.print("Finally!");
    }
}
```

Let’s disassemble it! `javap -v sample.class`

---

```txt
     0: new           #17  // class java/lang/Exception
     3: dup
     4: invokespecial #19  // Method java/lang/Exception."<init>":()V
     7: athrow
     8: astore_1
     9: getstatic     #20  // Field java/lang/System.out:Ljava/io/PrintStream;
    12: ldc           #26  // String Caught!
    14: invokevirtual #28  // Method java/io/PrintStream.print:(Ljava/lang/String;)V
    17: getstatic     #20  // Field java/lang/System.out:Ljava/io/PrintStream;
    20: ldc           #34  // String Finally!
    22: invokevirtual #28  // Method java/io/PrintStream.print:(Ljava/lang/String;)V
    25: goto          39
    28: astore_2
    29: getstatic     #20  // Field java/lang/System.out:Ljava/io/PrintStream;
    32: ldc           #34  // String Finally!
    34: invokevirtual #28  // Method java/io/PrintStream.print:(Ljava/lang/String;)V
    37: aload_2
    38: athrow
    39: return
```

---

And here’s the exception table:

```txt
     from    to  target type
         0     8     8   Class java/lang/Exception
         0    17    28   any
```

**First line**: That’s our try-catch! If an Exception is thrown between lines 0 to 8, go to the handler at line 8.

**Second line**: That’s our finally! If anything happens between line 0 to 17, go to the handler at line 28.

The flow goes like this: an Exception is created and then thrown on bytecode line 7. Exception table says, if this happens, go to line 8. Then, we just print out “Caught!” and “Finally!”, and goto line 39 where the method returns.

On bytecode lines 28 to 38, we have the finally rethrow protection.

---

## But wait, you said that an Exception is a possible **outcome of invoking the method**

What if I'm a functional developer? `Vavr` to the rescue!

```java
String getContent(String location) throws IOException {
    try {
        final URL url = new URL(location);
        if (!"http".equals(url.getProtocol())) {
            throw new UnsupportedOperationException("Not http");
        }
        final InputStream in = url.openConnection().getInputStream();
        return readAndClose(in);
    } catch(Exception x) {
        throw new IOException(
            "Error loading location " + location, x); }
}
```

---

## But wait, you said that an Exception is a possible **outcome of invoking the method**

The result of the `getContent(String)` method is either a _Success_ containing the content String or a _Failure_ containing an exception.

--

```java
Try<String> getContent(String location) {
    return Try
        .of(() -> new URL(location))
        .filter(url -> "http".equals(url.getProtocol()))
        .flatMap(url -> Try.of(url::openConnection))
        .flatMap(con -> Try.of(con::getInputStream))
        .map(this::readAndClose);
}
```

---

## But wait, you said that an Exception is a possible **outcome of invoking the method**

Or even better

--

```java
Try<String> getContent(String location) {
    return Try
        .of(() -> new URL(location))
        .filter(url -> "http".equals(url.getProtocol()))
        .mapTry(URL::openConnection)
        .mapTry(URLConnection::getInputStream)
        .map(this::readAndClose);
}
```

---

## But wait, you said that an Exception is a possible **outcome of invoking the method**

```java
Try<String> weTried = getContent("http://notsojug.github.io/");

String jugContent = weTried.getOrElse("JUG Offline");

String jugContent = weTried.getOrElseGet(
  throwable -> "Failed because " + throwable.getMessage());

String jugContent = weTried.getOrElseGet(
  throwable -> Match(throwable).of(
    Case(instanceOf(NoSuchElementException.class), "No http URL"),
    Case(instanceOf(IOException.class),
      "Failed because " + throwable.getMessage()),
    Case($(), "Something went wrong")));
```

---

## But wait, I WANT an Exception as a possible **outcome of invoking the method**

--

You write:

```java
static void validate(String name) throws ValidationException {
  // Validation code
}
```

--

I see:

```java
static ValidationResult validate(String name) {
  // Validation code
  return ValidationResult.VALID;
}
```

---

## Ok, what about Java 8? Functions don't like Checked exceptions!

**Java language specification**: Because a _functional interface_ contains only one abstract method, you can omit the name of that method when you implement it. To do this, instead of using an anonymous class expression, you use a _lambda expression_ [...]

> I like lambdas and method references... But `java.util.function.Function::apply` doesn't declare any Checked exception

`jOOλ` to the rescue! The **`Unchecked`** class can be used to wrap common `@FunctionalInterfaces` in equivalent ones that are allowed to throw checked exceptions.

---

## jOOλ Unchecked

```java
Arrays.stream(dir.listFiles()).forEach(file -> {
    try {
        System.out.println(file.getCanonicalPath());
    }
    catch (IOException e) {
        throw new RuntimeException(e);
    }
    // Ouch, my fingers hurt! All this typing!
});
```

--

... will become this beauty:

```java
Arrays.stream(dir.listFiles())
        .map(Unchecked.function(File::getCanonicalPath))
        .forEach(System.out::println);
```

---

## Links:

* http://www.slideshare.net/mariofusco/from-object-oriented-to-functional-domain-modeling/33
* https://shipilev.net/blog/2014/exceptional-performance/
* http://blog.me4502.com/exceptions-as-flow-control-in-java.html
* http://www.vavr.io/
* https://github.com/jOOQ/jOOL
* https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html

---

class: center, middle

# Questions

