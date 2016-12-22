package jug.swarm.swarmdemo.cdi;

import java.security.SecureRandom;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class RandomProducer {

  private static SecureRandom rnd = new SecureRandom();

  @Produces
  public SecureRandom produce() {
    return rnd;
  }

}
