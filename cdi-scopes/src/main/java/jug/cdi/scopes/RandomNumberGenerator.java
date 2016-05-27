package jug.cdi.scopes;

import java.security.SecureRandom;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class RandomNumberGenerator {

  private final SecureRandom random = new SecureRandom();
  private final String name = UUID.randomUUID().toString();

  @PostConstruct
  void init() throws Exception {
    System.out.println("@Application Scoped constructed: " + name);
  }
  
  public String getName() {
    return name;
  }

  @Produces
  @Random
  int getRandomNumber() {
    int nextInt = random.nextInt(100);
    System.out.println("Generated: " + nextInt);
    return nextInt;
  }

}
