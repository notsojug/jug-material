# Paradigm shift
## From imperative to functional and declarative

What's functional style?

* declarative
* high order functions
* honoring immutability
* pure functions

Why?

* concise
* expressive
* less code
* easier to understand, modify
* fewer bugs
* almost effortless parallelization and efficency

## 1. Deodorizing inner-classes

* Functional Interfaces

```java
public class Sample {
  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(10);
    for (int i = 0; i < 10; i++) {
      final int index = i;
      es.submit(new Runnable() {
        @Override
        public void run() {
          System.out.println("Task " + index);
        }
      });
    }
    System.out.println("Started");
    es.shutdown();
  }
}
```

```java
public class Sample {
  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(10);
    IntStream.range(0, 10)
      .forEach(i ->
        es.submit(() ->
          System.out.println("Task " + i)));
    System.out.println("Started");
    es.shutdown();
  }
}
```

## 2. Streamlining Iterations

* External vs. Internal iterators
* Specialized functions
* Function composition
* Infinite Streams
* Lazy evaluation

```java
public class Sample {
  public static boolean isPrime(int number) {
    boolean divisible = false;
    for (int i = 2; i < number; i++) {
      if (number % i == 0) {
        divisible = true;
        break;
      }
    }
    return number > 1 && !divisible;
  }
  public static void main(String[] args) {
    List<Double> sqrtOfFirst100Primes = new ArrayList<>();
    int index = 1;
    while (sqrtOfFirst100Primes.size() < 100) {
      if (isPrime(index)) {
        sqrtOfFirst100Primes.add(Math.sqrt(index));
      }
      index++;
    }
    System.out.println("Sqrt of first 100 prime: " +
        String.format("Computed %d, first %g, last %g",
            sqrtOfFirst100Primes.size(),
            sqrtOfFirst100Primes.get(0),
            sqrtOfFirst100Primes.get(sqrtOfFirst100Primes.size() - 1)));
  }
}
```

```java
public class Sample {
  public static boolean isPrime(int number) {
    return number > 1 && IntStream
        .range(2, number)
        .noneMatch(i -> number % i == 0);
  }
  public static void main(String[] args) {
    List<Double> sqrtOfFirst100Primes =
    Stream.iterate(1, e -> e + 1)
      .filter(Sample::isPrime)
      .map(Math::sqrt)
      .limit(100)
      .collect(Collectors.toList());
    System.out.println("Sqrt of first 100 prime: " +
        String.format("Computed %d, first %g, last %g",
            sqrtOfFirst100Primes.size(),
            sqrtOfFirst100Primes.get(0),
            sqrtOfFirst100Primes.get(sqrtOfFirst100Primes.size() - 1)));
  }
}
```

## 3. Tell, don't ask

* Keep focus on the whole instead of the part
* Code is trasparent
* Avoid the smell of null
* Optional

```java
public class Sample {
  static class TimeSlot {
    static Random random = new SecureRandom();
    public void schedule() {
      //...
    }
    public boolean isAvailable() {
      return random.nextBoolean();
    }
  }
  public static void main(String[] args) {
    List<TimeSlot> timeSlots = Arrays.asList(
        new TimeSlot(), new TimeSlot(), new TimeSlot());
    TimeSlot firstAvailable = null;
    for(TimeSlot timeSlot : timeSlots) {
      if (timeSlot.isAvailable()) {
        timeSlot.schedule();
        firstAvailable = timeSlot;
        break;
      }
    }
    System.out.println("First time slot: " + firstAvailable);
  }
}
```

```java
public class Sample {
  static class TimeSlot {...}
  public static void main(String[] args) {
    List<TimeSlot> timeSlots = Arrays.asList(
        new TimeSlot(), new TimeSlot(), new TimeSlot());
    TimeSlot firstAvailable = timeSlots.stream()
        .filter(TimeSlot::isAvailable)
        .findFirst()
        .orElseGet(TimeSlot::new);
    System.out.println("First time slot: " + firstAvailable);
  }
}
```

## 4. You gotta be kidding

* Simple tasks that haunt us
* Higher level of abstraction

```java
public class Sample {
  public static void main(String[] args) {
    List<Integer> numbers =
        Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    int[] factor = { 2 }; // effectively final?
    Stream<Integer> strm =
        numbers.stream().map(e -> e * factor[0]);
    factor[0] = 44;
    strm.forEach(System.out::println);
  }
}
```

```java
public class Sample {
  public static void main(String[] args) {
    File dir = new File(".");
    File[] children = dir.listFiles();
    if (children != null) {
      for (File child : children) {
        System.out.print(child.getName().toUpperCase() + ", ");
      }
      System.out.println();
    }
  }
}
```

```java
public class Sample {
  public static void main(String[] args) {
    File dir = new File(".");
    File[] children = dir.listFiles();
    if (children != null) {
      System.out.println(Stream.of(children)
        .map(File::getName)
        .map(String::toUpperCase)
        .collect(joining(", ")));
    }
  }
}
```


## 5. Non-intrusive comparisons

* Comparable vs. Comparator
* Composition of comparators

```java
public class Sample {
  static class Person implements Comparable<Person> {
    private final String name;
    private final int age;
    public Person(String name, int age) {
      this.name = name;
      this.age = age;
    }
    public Integer getAge() {return age;}
    public String getName() {return name;}
    @Override public String toString() {
      return String.format("%s -- %s", name, age);
    }
    @Override public int compareTo(Person other) {
      return Integer.valueOf(age).compareTo(other.age);
    }
  }
  public static void main(String[] args) {
    List<Person> people = Arrays.asList(
        new Person("Sara", 12),
        new Person("Mark", 43),
        new Person("Bob", 12),
        new Person("Jill", 64));
    Collections.sort(people);
    System.out.println(people);
  }
}
```

```java
public class Sample {
  static class Person implements Comparable<Person> {...}
  static void printSorted(List<Person> people, Comparator<Person> comp) {
    people.stream()
      .sorted(comp)
      .forEach(System.out::println);
  }
  public static void main(String[] args) {
    List<Person> people = Arrays.asList(
        new Person("Sara", 12),
        new Person("Mark", 43),
        new Person("Bob", 12),
        new Person("Jill", 64));
    printSorted(people, (p1, p2) -> p1.getAge().compareTo(p2.getAge()));
    printSorted(people, Comparator.comparing(Person::getAge));
    printSorted(people, Comparator.comparing(Person::getName));
    printSorted(people, Comparator.comparing(Person::getName)
        .thenComparing(Person::getAge));
  }
}
```

## 6. Execute around methor pattern

* Garbage collection
* Resource cleanup
* ARM (Automatic Resource Management, [Java 7](http://www.oracle.com/technetwork/articles/java/trywithresources-401775.html))
* Deterministic behavior

```java
public class Sample {
  static class Resource implements AutoCloseable {
    public Resource() { System.out.println("Creating"); }
    public void op1() { System.out.println("Operation 1"); };
    public void op2() { System.out.println("Operation 2"); };
    public void close() { System.out.println("Resource closed"); }
  }
  public static void main(String[] args) {
    try (Resource resource = new Resource()) {
      resource.op1();
      resource.op2();
    }
  }
}
```

```java
public class Sample {
  static class Resource {
    private Resource() { System.out.println("Creating"); }
    public Resource op1() { System.out.println("Operation 1"); return this; };
    public Resource op2() { System.out.println("Operation 2"); return this; };
    private void close() { System.out.println("Resource closed"); }
    public static void use(Consumer<Resource> block) {
      Resource resource = new Resource();
      try { block.accept(resource); }
      finally { resource.close(); }
    }
  }
  public static void main(String[] args) {
    Resource.use(resource -> {
      resource.op1().op2();
    });
  }
}
```

---

Kudos to: Dr. Venkat Subramaniam https://www.youtube.com/watch?v=wk3WLaR2V2U