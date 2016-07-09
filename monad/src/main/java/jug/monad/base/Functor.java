package jug.monad.base;

import java.util.function.Function;

public interface Functor<T> {

  <R> Functor<R> map(Function<T, R> f);

}