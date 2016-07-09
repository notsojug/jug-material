package jug.monad.advanced;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class FPromise<T> implements Functor<T, FPromise<?>> {

  private final Supplier<T> valueSupplier;

  private FPromise(Supplier<T> valueSupplier) {
    this.valueSupplier = valueSupplier;
  }

  public <R> FPromise<R> map(Function<T, R> f) {
    return of(() -> f.apply(valueSupplier.get()));
  }

  public static <T> FPromise<T> of(Supplier<T> slowSupplier) {
    return new FPromise<T>(slowSupplier);
  }
  
  @Override
  public String toString() {
    return Objects.toString(valueSupplier.get());
  }

}
