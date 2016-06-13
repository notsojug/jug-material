class: center, middle

# Stream API

Collection processing in a functional way.

---

# Collection interface


Did we mention `default` methods? This is why they have been introduced:

```
public interface Collection<E> extends Iterable<E> {

    // blah blah

	/**
	 * Returns a sequential {@code Stream} 
	 * with this collection as its source.
	 */
	default Stream<E> stream() {...}

}
```

---

# What is a stream?


Quoting `java.util.Stream<E>` :

	A sequence of elements supporting sequential and 
	parallel aggregate operations.

--
	
	To perform a computation, stream operations are 
	composed into a stream pipeline. 

---

# What is a stream pipeline?

A stream/pipeline consists in:

- a source (array, collection, generator fuction, I/O, ...)
- zero or more intermediate operation (filtering, transformation, ...)
- a terminal operation (list collectors, summing, counting, for-each consumers, ...)
	
	
--

	Streams are lazy; computation on the source data is 
	only performed when the terminal operation is initiated, 
	and source elements are consumed only as needed.

---

### Stream sources:

```
// arrays:
String[] array = ...;
Stream<String> aStream = Arrays.stream(array);
Stream<String> anotherStream = Stream.of("one", "two", "three");

// collections
Stream<String> = Lists.newArrayList(...).stream();

// I/O
Stream<String> lines = Files.lines(Paths.get("tmp", "file"));

// Generators
Stream<String> stream = Stream.generate(()-> new String());

// Iterators(usually seed + unary operator)
Stream<String> stream = Stream.iterate("Hello", s -> s + "o");
```

---

### Stream sources:

```
IntStream.range(1, 101) // 1,2, ... 100
IntStream.rangeClosed(1, 100) // 1,2, ... 100

IntStream.iterate(2, x -> x + 5) 
// 2, 7, 12, .... (infinite unless limited)

(new Random()).ints() // infinite stream of randomness

```
... and so on and so on ...


---

### Stream intermediate operations

.left60[
* `filter`
* `map`
* `mapTo... (Int, Long or Double)`
* `flatMap`
* `flatMapTo... (Int, Long or Double)`
* `distinct`
* `sorted`
]
.right40[
* `peek`
* `limit`
* `skip`
* `sequential`
* `parallel`
* `unordered`
* `onClose`
]

---

### Stream terminal operations

.left50[
* `forEach`
* `forEachOrdered`
* `toArray`
* `reduce`
* `collect`
* `min`
* `max`
* `count`
]
.right50[
* `anyMatch`
* `allMatch`
* `noneMatch`
* `findFirst`
* `findAny`
* `iterator`
* `spliterator`
]

---

### Intermediate operations: filter

Filter keeps the elements that satisfy the predicate.

```
Stream.of(1, 5, 7, 8, 10)
	.filter(x -> x % 2 == 0)
	// from here on, stream forwards even numbers only
	.toArray(); // [8, 10]
```

---

### Intermediate operations: map

Map transforms the elements according to a function 

```
Stream.of(1, 5, 7, 8, 10)
	.map(x -> x * 2)
	// from here on, stream doubles every element
	.toArray(); // [2, 10, 14, 16, 20]
```

Function can also change the nature of the object, extract a field, or get the value externally.

```
Stream.of(1, 5, 7, 8, 10)
	.map(Object::toString)
	// from here on, stream converts to string each elements
	.toArray(); // ["1", "5", "7", "8", "10"]
```

---

### Intermediate operations: skip/limit

Skip a number of elements
```
Stream.of(1, 5, 7, 8, 10)
	.skip(2)
	.toArray(); // [7, 8, 10]
```

Limit the total elements
```
Stream.of(1, 5, 7, 8, 10)
	.limit(2)
	.toArray(); // [1, 5]
```
---

### Intermediate operations: distinct, sorted

Keep unique values
```
Stream.of(1, 5, 5, 8, 8)
	.distinct()
	.iterator(); // [1, 5, 8]
```

Order elements
```
Stream.of(8, 1, 2, 6)
	.sorted()
// or sorted(Comparator...) 
// or sorted(Comparator.comparing(Function<T, U> keyExtractor))
	.iterator(); // [1, 2, 6, 8]
```

---

### Intermediate operations: flatMap

What if we extract a Stream from a map operation?

```
Stream.of(1, 5, 7, 8, 10)
	.map(x -> IntStream.range(1, x).boxed())
	// here we have Stream<Stream<Integer>> ...what now?
```
--

**flatMap**!
```
Stream.of(1, 5, 7, 8, 10)
	.flatMap(x -> IntStream.range(1, x).boxed())
	// here we have Stream<Integer>
	// the main stream "pauses" and let the inner 
	// stream going on until consumed
```

---
### Intermediate operations: peek

Peek: inspect elements without consuming them
```
Stream.of(1, 5, 7, 8, 10)
	.peek(System.out::println) // 1, 5, 7, 8, 10
	.map(x -> x * 2) // 2, 10, ...
```

---
### Terminal operations: reduce


```
ArrayList<Integer> reduced  = Stream
  .of(1, 5, 7, 8, 10)
  .reduce(
    // start from initial value
    new ArrayList<Integer>(),  
    // combine next element of the stream
    (ArrayList<Integer> x, Integer y) -> { x.add(y); return x; }, 
    // combine two values
    (ArrayList<Integer> l1, ArrayList<Integer> l2) -> { 
    	l1.addAll(l2); 
    	return l1; 
    });
```
--
Shouldn't it be easier to build a list?

---
### Terminal operations: collect

`collect` accepts a generic `java.util.stream.Collector`.

Built-in collectors are found in `Collectors` class, e.g.:

```
List<Integer> list = Stream
  .of(1, 5, 7, 8, 10)
  .collect(Collectors.toList());
```
--

Or

```
List<Integer> list = Stream
  .of(1, 5, 7, 8, 10)
  .collect(Collectors.partitioningBy(x -> x % 2 == 0));
```

---

# Caveats

- filter early, compute as late as possible 
- infinite streams/subtle infinite streams: use limit accordingly
- skip/limit order
- streams cannot be reused
- don't modify the initial collection in the pipeline
- don't forget to call a terminal operation

---

## Links:

* Stream ([Javadoc](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html))
* Collectors ([Javadoc](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html))
* 10 mistakes to avoid ([jOOQ blog](https://blog.jooq.org/2014/06/13/java-8-friday-10-subtle-mistakes-when-using-the-streams-api/))
* Streams in java8 ([Oracle](http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html))
* What's wrong with java8 ([Dzone](https://dzone.com/articles/whats-wrong-java-8-part-iii))

---

class: center, middle

# Questions
