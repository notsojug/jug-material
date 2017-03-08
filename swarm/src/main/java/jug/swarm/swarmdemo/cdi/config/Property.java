package jug.swarm.swarmdemo.cdi.config;

import static jug.swarm.swarmdemo.cdi.config.Patterns.matches;
import static jug.swarm.swarmdemo.cdi.config.Patterns.everTrue;

import viper.CdiConfiguration;
import viper.PropertyFileResolver;

import java.util.function.Predicate;

import javax.enterprise.context.ApplicationScoped;

@CdiConfiguration.PassAnnotations({ApplicationScoped.class, Eager.class})
@CdiConfiguration
@PropertyFileResolver(propertiesPath = "/opt/swarm/my.config")
public enum Property {

  @CdiConfiguration.DefaultKey
  APPID("appid", matches("[A-Z0-9]+_[A-Z0-9]+")),
  TIMEOUT("timeout", matches("[0-9]+")),
  URL("url", everTrue());

  String suffix;
  Predicate<String> validator;

  private Property(String suffix, Predicate<String> validator) {
    this.suffix = suffix;
    this.validator = validator;
  }

  private Property() {
    this.suffix = "";
    this.validator = s -> true;
  }

  @PropertyFileResolver.KeyString
  public String key() {
    return "jug.swarm." + suffix;
  }

  @CdiConfiguration.ConfigValidator
  public Predicate<String> validator() {
    return validator;
  }
}
