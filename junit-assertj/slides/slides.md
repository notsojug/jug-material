class: center, middle

# JUnit

## A simple framework to write repeatable tests.

---

Also...

.center[
# AssertJ
]

## A tool to fluently write assertions on tests, and generate those assertions from your code.

---

## What is a test?

A piece of code you write _now_ to verify a piece of code you have written before. 

--

Sounds like a crazy idea, right?

--

It turns out it isn't, after all...

---

## Example

```
public class Calculator {
	public int divide(int a, int b) {
		return a/b;
	}
}
```

--

Let's test it...

---

# Example 

```
public class CalculatorTest {
	@org.junit.Test
	public void shouldDivideByTwo() throws Exception {
		Calculator c = new Calculator();
		Assert.assertEquals(5, c.divide(10,2));
	}
...
```
--
And maybe:
```
	@org.junit.Test
	public void shouldDivideBy2WithRounding() throws Exception {
		Calculator c = new Calculator();
		Assert.assertEquals(6, c.divide(13,2));
	}
```



---

# Oh boy, that was _very_ hard...

--

# ... now refactor to proper structure

---

## org.junit.Before for setup method

```
public class CalculatorBetterTest {
	
	Calculator cut; // Class Under Test
	
	@org.junit.Before
	public void setUp(){
		cut = new Calculator(); // tedious initialization bits here...
	}
	
	@org.junit.Test
	public void shouldDivideByTwo() throws Exception {
		Assert.assertEquals(5, cut.divide(10, 2));
	}
	...
}
```

This will be executed **before each test method**. 

???
The annotation is important, the method name is not.

---

## org.junit.After for tear down method

```
@org.junit.After
public void tearDown(){
	System.out.println("Clean the mess before the next test(s)");
}
```

This will be executed **after each test method**.

Think about this as a way to clean after a test. 

???

Of course you won't need this if the `@Before` just declares `new Calculator()`...

--

But my object/surrounding objects initialization cost a lot!

---

## org.junit.BeforeClass org.junit.AfterClass annotations  

```
@org.junit.BeforeClass
public static void heavyLifting(){
	System.out.println("This is a CPU heavy method");
}
```

This will be executed **before the first test** and before the `@Before`-annotated method.

It has to be static.

The `@AfterClass` is the same, except it is run after all the tests and after the last `@After`-annotated method.

---

## Conventions and advices

- Test one business class per test class
- Make each test method isolated and repeatable (i.e. can be launched alone)
- Do not reuse static variables of a test class
- Don't optimize tests, make them right before making them fast
- Make single test methods as short as possible, make lots of them
- Each test _should_ assert _one_ thing about the result(s).
	That one thing may be the content of a whole object, but still... 
- Test classes' names should start or finish with `Test`
- Integration test classes' names should start or finish with `IT`

This is not enforced, but...

---

## Maven

Maven loves to run your tests. Let it do its job.

Plugins:

- surefire
	+ it runs unit tests (the ones that ends in `Test`, unless configured)
	+ it runs after `compile`, stopping the build if tests fails 
- failsafe
	+ it runs integration tests (the ones ending in `IT`, unless configured)
	+ it runs in the `verify` phase, after `package`
	
i.e. `maven package` will run surefire plugin and not failsafe plugin.

---

# Thou shall not skip tests

If maven lets you run every test you have accumulated during the product history, why skipping tests? They (should) let you know you have not broken those tested behaviours...   

---
## Beyond basics: testing exceptions

If you want to test if an exception is thrown:

```
@Test(expected = ArithmeticException.class)
public void dividingByZeroShouldThrowArithmeticException() throws Exception {
	int a = 5 / 0;
}
```

--

What if we want to assert something on the message?

--

Well, you guessed it: you have to catch it manually (and good luck with that!).

---

## Beyond basics: timeout

```
@Test(timeout=1000)
public void testWithTimeout() {
  // test code here
}
```

This test fails (also) if it takes more than 1000 milliseconds.

---

## Beyond basics: testing with files

	Yeah, but i deal with filesystem, i can not unit test
--

Wrong

---

### Beyond basics: testing with files

```
@org.junit.Rule
public TemporaryFolder folder = new TemporaryFolder();

@Test
public void createAFileYo() throws Exception {
	File root = folder.getRoot();
	File yo = new File(root, "yo.somefile");
	yo.createNewFile();
	assertTrue(yo.exists());
}

@Test
public void theYoFileShouldNotExistHere() throws Exception {
	File root = folder.getRoot();
	File yo = new File(root, "yo.somefile");
	assertFalse(yo.exists());
}
```

???

Directory is created and deleted before and after each method, so each test is indipendent.

---

### Beyond basics: rules

Rules are way to extend junit behaviour.

Rules exist for some common patterns:

- **ErrorCollector**: collect multiple errors in one test method
- **ExpectedException**: make flexible assertions about thrown exceptions
- **ExternalResource**: start and stop a server, for example
- **TemporaryFolder**: create fresh files, and delete after test
- **TestName**: remember the test name for use during the method
- **TestWatcher**: add logic at events during method execution
- **Timeout**: cause test to fail after a set time
- **Verifier**: fail test if object state ends up incorrect

Just declare the rule as a field of the test class, configure it if needed, and go on testing.

---

### Beyond basics: method ordering

```
@org.junit.FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMethodOrder {

    @Test
    public void testA() { System.out.println("first"); }
    
    @Test
    public void testB() { System.out.println("second"); }
    
    @Test
    public void testC() { System.out.println("third"); }
}
```

You can provide a MethodSorter of your creation, if you need one.

---

### Beyond basics: test runners

By default the junit flow is managed by the JUnit runner, which takes care of invoking the `@Before`, the `@After`, etc... but it can be extended!

Notable readily available runners:

- **Parameterized**  To run so-called property-testing tests.
- **Suite** To organize test classes in suites.
- **Category** To mark tests with a particular category, permitting you to select tests.
- **PowerMockRunner** To operate on bytecode of hard-to-test classes (static fields? singletons?)

See examples on the internet or in your business codebase ;)

---

## Other features

- ignoring a single test: don't even think about it
- Assumptions: skip tests if a particular condition is not met

---

## Other projects

- Automatic test of hashCode, equals, etc... via extensions (see links)
- Continuous testing via IDE: Infinitest
- Test coverage: how many lines and paths did I cover?? Eclemma/jacoco

---

## Links:

* http://junit.org/junit4/
* https://github.com/junit-team/junit4/wiki/Parameterized-tests
* https://github.com/junit-team/junit4/wiki/Multithreaded-code-and-concurrency
* http://junit-addons.sourceforge.net/junitx/extensions/package-frame.html
* https://infinitest.github.io/

--

JUnit 5 is out!

---

# What about assertj?

---

# Assertj

---

class: center, middle

# Questions

