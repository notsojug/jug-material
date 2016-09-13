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

---

## Links:

* http://junit.org/junit4/


---

class: center, middle

# Questions

