class: center, middle

# Apache Maven

## A software project management and comprehension tool
_Some useful advices_

---

# Apache

Maven (_Yiddish_ word meaning _accumulator of knowledge_) was originally started as an attempt to simplify the build processes in the Jakarta Turbine project.

## Objectives:

* Making the build process **easy**
* Providing a **uniform** build system
* Providing **quality** project **information**
* Providing guidelines for **best practices development**
* Allowing **transparent migration** to new features

---

# Build Lifecycle Basics

The process for building and distributing a particular artifact (project) is clearly defined.

Three built-in build lifecycles:

* `default` lifecycle handles your project deployment
* `clean` lifecycle handles project cleaning
* `site` lifecycle handles the creation of your project's site documentation

Each of these build lifecycles is defined by a different list of **build phases**, wherein a build phase represents a **stage** in the lifecycle.

---

# Build Lifecycle Basics

* `validate` validate the project is correct and all necessary information is available
* `compile` compile the source code of the project
* `test` test the compiled source code using a suitable **unit testing** framework. These tests _should not require the code be packaged_ or deployed
* `package` take the compiled code and package it in its distributable format, such as a JAR.
* `verify` run any checks on results of **integration tests** to ensure quality criteria are met
* `install` install the package into the local repository, for use as a dependency in other projects locally
* `deploy` done in the build environment, copies the final package to the remote repository for sharing with other developers and projects.

---

## A Build Phase is Made Up of Plugin Goals

However, even though a build phase is responsible for a specific step in the build lifecycle, the manner in which it carries out those responsibilities may vary. And this is done by declaring the plugin goals bound to those build phases.

A plugin goal represents a specific task (finer than a build phase) which contributes to the building and managing of a project. **It may be bound to zero or more build phases**.

Code coverage tools such as **Jacoco** and execution container plugins such as **Tomcat, Cargo, and Docker** bind goals to the `pre-integration-test` phase to prepare the integration test container environment. These plugins also bind goals to the `post-integration-test` phase to collect coverage statistics or decommission the integration test container.

???
The phases named with hyphenated-words (`pre-*`, `post-*`, or `process-*`) are not usually directly called from the command line.

---

## Packaging

The first, and most common way, is to set the packaging for your project via the equally named POM element `<packaging>`. Some of the valid packaging values are `jar`, `war`, `ear` and pom. If no packaging value has been specified, it will default to `jar`.

Each packaging contains a list of goals to bind to a particular phase. For example, the `jar` packaging will bind the following goals to build phases of the default lifecycle:

(next)

---

## Packaging

```txt
process-resources => resources:resources
compile => compiler:compile
process-test-resources => resources:testResources
test-compile => compiler:testCompile
test => surefire:test
package => jar:jar
install => install:install
deploy => deploy:deploy
```

If a project that is purely metadata (packaging value is pom) only binds goals to the install and deploy phases.

For a complete list of goal-to-build-phase bindings of some of the packaging types, refer to the [Lifecycle Reference](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference).

---

## Plugins

Plugins are artifacts that provide goals to Maven. Furthermore, a plugin may have one or more goals wherein each goal represents a capability of that plugin. For example, the Compiler plugin has two goals: *compile* and *testCompile*. The former compiles the source code of your main code, while the latter compiles the source code of your test code.

Plugins can contain information that indicates which lifecycle phase to bind a goal to. **Note that adding the plugin on its own is not enough information - you must also specify the goals you want to run as part of your build.**

Note that you can use the `<executions>` element to gain more control over the order of particular goals.

---

## Plugins

```
 <plugin>
   <groupId>...</groupId>
   <artifactId>...</artifactId>
   <version>...</version>
   <executions>
     <execution>
       <configuration>
         ...
       </configuration>
       <phase>process-test-resources</phase>
       <goals>
         <goal>java</goal>
       </goals>
     </execution>
   </executions>
 </plugin>
```

---

# Examples

#### Maven Surefire Plugin

The Surefire Plugin is used during the `test` phase of the build lifecycle to execute the unit tests of an application.

#### Maven Failsafe Plugin

The Failsafe Plugin is designed to run integration tests:

* `pre-integration-test` for setting up the integration test environment.
* `integration-test` for running the integration tests.
* `post-integration-test` for tearing down the integration test environment.
* `verify` for checking the results of the integration tests.

---

## Maven Surefire Plugin

[Usage](https://maven.apache.org/surefire/maven-surefire-plugin/usage.html) • [JUnit](https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html)

My plugin configuration:

```
<plugin>
   <groupId>org.apache.maven.plugins</groupId>
   <artifactId>maven-surefire-plugin</artifactId>
   <configuration>
      <forkCount>2.5C</forkCount>
      <reuseForks>false</reuseForks>
   </configuration>
</plugin>
```

---

## Maven Failsafe Plugin

[Usage](https://maven.apache.org/surefire/maven-failsafe-plugin/usage.html) • [JUnit](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/junit.html)

My plugin configuration:

```
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-failsafe-plugin</artifactId>
  <executions>
    <execution>
      <goals>
        <goal>integration-test</goal>
        <goal>verify</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

---

# Which version?

* <3.0??
* 3.0?
* 3.3?

# Which Java version?

* 1.6?
* 1.7?
* 1.8?
* 1.9??

--

Develop or productiont?
--
What if they are different?

---

# The JDK choice dilemma

* **Developer temptation**: recent build tools, with more features, requiring recent JDK
* **Manager requirement**: ensure compatibility with target environment: often quite old JRE (and the same on whole landscape?)


--

* Miscellaneous strategies:
 * **Conservatory**: JDK = min( JRE&lt;every apps&gt; ) + old associated tools...
 * **Courageous**: for each app, switch JDK & associated tools
 * **Player** (unaware?): recent JDK
 * **Serious**: recent JDK + CI and deep tests, with good coverage
 * **Smart**: Maven + a few configurations

---

# Bytecode version

**Binary backward compatibility**

* JVM can execute old bytecode
* but not newer bytecode, or... `java.lang.UnsupportedClassVersionError`

`.class` file format:
4 bytes: magic number - 2 bytes: minor version - 2 bytes: major version

* Java 8 = `52 (0x34)`
* Java 7 = `51 (0x33)`
* Java 6 = `50 (0x32)`
* Java 5 = `49 (0x31)`

---

# Bytecode version, Maven + javac

### Javac

* Default: bytecode version = JDK version
* `-target` defines bytecode version

### Maven

Maven allows to easily control the version of the bytecode used in the project build
* Default: maven-compiler-plugin sets `–target 1.5`
* maven-compiler-plugin’s target parameter

???
Maven default is independent from JDK used to run

---

# Problem

What if we have different JDK version for develop, run maven, compile and run in production?

---

# Solution

What if we have different JDK version for develop, run maven, compile and run in production?

Maven allows also to verify the version of the bytecode used in project dependencies!

`enforceBytecodeVersion` rule from `maven-enforcer-plugin`

**Sample**: http://www.mojohaus.org/extra-enforcer-rules/enforceBytecodeVersion.html

---

# Other tools

With Java evolution going faster the need to mixup different versions of Java will be more important and will increase the risk of incompatibilities.


* With Maven you are ready to:
 * automatically verify the compatibility by using _Animal Sniffer_ and the _Enforcer_ plugin
 * use the right JDK targeted by your application by configuring the _Toolchains_


[Enforcer](https://maven.apache.org/enforcer/maven-enforcer-plugin/) • [Animal Sniffer](http://mojohaus.org/animal-sniffer/) • [Toolchains](https://maven.apache.org/guides/mini/guide-using-toolchains.html)

---

# Authomatic style checker

Why an automated style checker?

### Problems:
too many people, too many lines of code.

### Solutions:
code reviews? unit tests? integration tests?

--

## Yes! Plus style guides and checkstyle!

---

# Checkstyle

Checkstyle is a **development tool** to help programmers write Java code that adheres to a coding standard.

It automates the process of checking Java code to spare humans of this _boring (but important)_ task. This makes it ideal for projects that want to **enforce a coding standard**.

Checkstyle is highly configurable and can be made to support almost **any coding standard**.

For a detailed list of available checks refer to the [Checks](http://checkstyle.sourceforge.net/checks.html) page.

---

# Checkstyle, where to use?

### Eclipse: Checkstyle Plugin (aka eclipse-cs)

Within the Eclipse workbench you are immediately notified of problems via the Eclipse Problems View and source code annotations similar to compiler errors or warnings.

This ensures an **extremely short feedback loop right at the developers fingertips**.

---

# Checkstyle, where to use?

### Eclipse: Checkstyle Plugin (aka eclipse-cs)

**Why would I use it?**

* In a big development team (obviously) a common ground for coding standards must be agreed upon - even if it is just for practical reasons to avoid superficial, format related merge conflicts.
* Checkstyle (and the Eclipse Checkstyle Plugin) helps you define and easily apply those common rules.

Project home: http://eclipse-cs.sourceforge.net/

---

# Checkstyle, where to use?

### Maven: Maven Checkstyle Plugin

The Checkstyle Plugin has three goals:

* `checkstyle:checkstyle` is a reporting goal that performs Checkstyle analysis and generates a report on violations.
* `checkstyle:checkstyle-aggregate` is a reporting goal that performs Checkstyle analysis and generates an aggregate HTML report on violations in a multi-module reactor build.
* `checkstyle:check` is a goal that performs Checkstyle analysis and outputs violations or a count of violations to the console, **potentially failing the build**. It can also be configured to re-use an earlier analysis.

---

# Checkstyle, where to use?

### Maven: Maven Checkstyle Plugin

```
<executions>
   <execution>
   <id>validate</id>
   <phase>validate</phase><goals><goal>check</goal></goals>
   <configuration>
      <configLocation>google_checks.xml</configLocation>
      <encoding>UTF-8</encoding>
      <consoleOutput>true</consoleOutput>
      <failsOnError>true</failsOnError>
   </configuration>
   </execution>
</executions>
```

???
https://maven.apache.org/plugins/maven-checkstyle-plugin/usage.html

---

# Links:

* [Java is evolving rapidly](http://www.slideshare.net/aheritier/java-is-evolving-rapidly-maven-helps-you-staying-on-track)
* [Eclipse Checkstyle Plugin](http://eclipse-cs.sourceforge.net/)
* [Maven Checkstyle Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/)

---

class: center, middle

# Questions
