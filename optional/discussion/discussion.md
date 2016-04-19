
# Questions

1. If you don't call `something.orElse(default)`, the inner value is `null`
  - the `get()` method throws an exception if the inner value is absent.
2. Is the default value in `orElse(default)` more dangerous than `null` itself?
  - Of course you should handle your optionals with care; Returning `Optional<>` forces the caller to think about the presence of a value, rather than forcing it to think of the semantic meaning of a `null` reference (which, in turn may not have any sense...)
3. Does it make sense to use Optional to return a default value in a method?
  - yes, if there is a reason always return a value. Returning an `Optional<>` lets the caller decide what to do in the absence of a value.
  You can alway do something like:

  ```java
  /**
   * The default is  0
   */
  Integer alwaysReturnSomething(String key) {
    Integer maybeNull = map.get(Preconditions.checkNotNull(key));
    return Optional.ofNullable(maybeNull).orElse(0);
  }
  ```
