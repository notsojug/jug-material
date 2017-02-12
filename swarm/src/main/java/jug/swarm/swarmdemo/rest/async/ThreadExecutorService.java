package jug.swarm.swarmdemo.rest.async;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class ThreadExecutorService implements ExecutorService {

  @Override
  public Future<?> submit(Runnable task) {
    new Thread(task).start();
    return null;
  }



  @Override
  public void execute(Runnable command) {
    throw new IllegalStateException();
  }

  @Override
  public void shutdown() {
    throw new IllegalStateException();
  }

  @Override
  public List<Runnable> shutdownNow() {
    throw new IllegalStateException();
  }

  @Override
  public boolean isShutdown() {
    throw new IllegalStateException();
  }

  @Override
  public boolean isTerminated() {
    throw new IllegalStateException();
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    throw new IllegalStateException();
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    throw new IllegalStateException();
  }

  @Override
  public <T> Future<T> submit(Runnable task, T result) {
    throw new IllegalStateException();
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
      throws InterruptedException {
    throw new IllegalStateException();
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout,
      TimeUnit unit) throws InterruptedException {
    throw new IllegalStateException();
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
      throws InterruptedException, ExecutionException {
    throw new IllegalStateException();
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    throw new IllegalStateException();
  }

}
