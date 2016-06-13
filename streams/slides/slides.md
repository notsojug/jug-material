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
	
	
---
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

### Stream operations

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
* 10 mistakes to avoid ([jOOQ blog](https://blog.jooq.org/2014/06/13/java-8-friday-10-subtle-mistakes-when-using-the-streams-api/))
* Streams in java8 ([Oracle](http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html))
* What's wrong with java8 ([Dzone](https://dzone.com/articles/whats-wrong-java-8-part-iii))

---

class: center, middle

# Questions
