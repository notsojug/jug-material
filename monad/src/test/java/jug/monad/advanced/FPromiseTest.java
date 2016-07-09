package jug.monad.advanced;

import java.util.function.Supplier;

import org.junit.Test;

public class FPromiseTest {
  
  Supplier<String> slowSupplier = () -> {
    try {
      Thread.sleep(5000L);
    } catch (Exception e) {
      e.printStackTrace();
    }
    final String value = "test";
    System.out.println("Produced: " + value);
    return value;
  };
  
  @Test
  public void fastTransformationTest() throws Exception {
    System.out.println("Promise creation");
    FPromise<String> f = FPromise.of(slowSupplier);
    System.out.println("1st map");
    FPromise<String> trans_1 = f.map(String::toUpperCase);
    System.out.println("2nd map");
    FPromise<Integer> trans_2 = trans_1.map(String::length);
    System.out.println("The content: " + trans_2);
  }
  
  @Test
  public void slowTransformationTest() throws Exception {
    System.out.println("Object creation");
    final String object = slowSupplier.get();
    System.out.println("1st map");
    final String trans_1 = object.toUpperCase();
    System.out.println("2nd map");
    final int trans_2 = trans_1.length();
    System.out.println("The object: " + trans_2);
  }
}
