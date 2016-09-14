# Code material for JUG

![Build status for JUG](https://api.travis-ci.org/civitz/jug-material.svg)

This project holds code for the JUG, feel free to fork.

## Modules

Each module represents a talk of the JUG

- Demo: an empty, disposable module for the next talk
- Guava: a collection of utilities from Google
- Optional: a design pattern for values that may be not present (towards functional programming)
- Throwable: a talk on how (not) to use exceptions and related subjects
- Retrofit: an awesome library to build type-safe, easy-to-read http clients
- Immutables: immutable data is king, and a framework helps us build immutable objects easily
- SOLID-SRP: an exploration of the Single Responsibility Principle with an example
- SOLID-IOC: a tour of Inversion of Control/ Dependency Inversion /Hollywood Principle
- JavaEE-Inject: an introduction for dependency injection in a Java EE container
- JavaEE-more concepts: advanced concepts in the java EE spec
- Lambda: an exploration of the lambdas in the java programming language
- Streams: the stream API from java 8
- JAX-RS: Java API for RESTful Web Services in JavaEE 6
- Monads: a bit of functional programming in Java 8
- Monadic Model: introduction to Monadic Model and JAVASLANG
- JUnit and AssertJ: a fast intro to test goodies

## Contributing

Each accepted talk can (should?) have its own subproject/submodule.

### Buildable modules

Each module should compile with a `mvn clean test` in the main directory, and optionally be buildable per-se (`cd <modulename> && mvn clean test`).

### Pull request and travis-ci

Each PR should build with travis-ci:

- travis-ci only issues a `mvn clean test` at the root of the project, so each subproject must also be mentioned as a module.
- if you encounter any issues because of the travis-ci configuration, please mention it in the PR and you will be contacted.

### Dependencies

- Add each module dependency in the module's `pom.xml`.
- Dependencies in main `pom.xml` will be scrutinized: only those compatible with java 6 AND requested by the majority of the submodules will be accepted in the main `pom.xml`
