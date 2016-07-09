package jug.monad.advanced;

import java.util.function.Function;

public interface Functor<T, F extends Functor<?, ?>> {
  
  <R> F map(Function<T, R> f);
  
}