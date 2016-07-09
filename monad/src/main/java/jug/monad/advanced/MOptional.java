package jug.monad.advanced;

import java.util.Objects;
import java.util.function.Function;

public class MOptional<T> implements Monad<T, MOptional<?>> {

  private final T valueOrNull;

  private MOptional(T valueOrNull) {
    this.valueOrNull = valueOrNull;
  }

  @Override
  public <R> MOptional<R> map(Function<T, R> f) {
    if (valueOrNull == null) return empty();
    else return of(f.apply(valueOrNull));
  }

  @Override
  public MOptional<?> flatMap(Function<T, MOptional<?>> f) {
    if (valueOrNull == null) return empty();
    else return f.apply(valueOrNull);
  }

  public static <T> MOptional<T> of(T a) {
    return new MOptional<T>(a);
  }
  public static <T> MOptional<T> empty() {
    return new MOptional<T>(null);
  }
  
  @Override
  public String toString() {
    return Objects.toString(valueOrNull);
  }

}
