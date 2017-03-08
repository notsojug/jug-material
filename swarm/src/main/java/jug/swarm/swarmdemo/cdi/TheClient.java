package jug.swarm.swarmdemo.cdi;


import jug.swarm.swarmdemo.cdi.config.Configuration;
import jug.swarm.swarmdemo.cdi.config.Property;

import javax.inject.Inject;

public class TheClient {

  private String appid;
  private int timeout;
  private String url;

  @Inject
  public TheClient(@Configuration(Property.APPID) String appid,
      @Configuration(Property.TIMEOUT) String timeout, @Configuration(Property.URL) String url) {
    super();
    this.appid = appid;
    this.timeout = Integer.valueOf(timeout);
    this.url = url;
  }

  @Override
  public String toString() {
    return "TheClient [appid=" + appid + ", timeout=" + timeout + ", url=" + url + "]";
  }

}
