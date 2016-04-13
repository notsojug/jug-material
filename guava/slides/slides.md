class: center, middle

# Guava

---

# What/Who?

Guava is a fruit.

--

Just kidding... it's Google's collection of utilities.

--

Battle-tested in production.

Well unit-tested (286,000 test cases, as of 2014).

---

# Why?

Java 6 target

Simple, clean code

Sane defaults

---

# So what?

- Collection utilities 
- Defensive coding aids
- (limited) functional-style programming
- ...

---

## Example: Collections constructors 

```
List<String> list = 
	Lists.newArrayList("one", "two", null, "yo");
```
```
Set<Integer> set = Sets.newHashSetWithExpectedSize(10);
```
```
Comparator<String> c = /* ... */
Map<String, Elephants> map = Maps.newTreeMap(c);
```