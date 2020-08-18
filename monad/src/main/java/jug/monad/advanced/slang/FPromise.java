package jug.monad.advanced.slang;

import java.util.Objects;
import java.util.function.Function;

import io.vavr.Function0;
import jug.monad.advanced.Functor;

public class FPromise<T> implements Functor<T, FPromise<?>> {

  private final Function0<T> valueSupplier;

  private FPromise(Function0<T> valueSupplier) {
    this.valueSupplier = valueSupplier;
  }

  public <R> FPromise<R> map(Function<T, R> f) {
    return of(valueSupplier.andThen(f));
  }

  public static <T> FPromise<T> of(Function0<T> slowSupplier) {
    return new FPromise<T>(slowSupplier);
  }

  @Override
  public String toString() {
    return Objects.toString(valueSupplier.get());
  }

}
