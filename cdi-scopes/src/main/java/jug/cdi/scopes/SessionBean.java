package jug.cdi.scopes;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import com.google.common.base.MoreObjects;

@SessionScoped
public class SessionBean implements Serializable {

  private static final long serialVersionUID = 7178716836022001730L;
  private final String name = UUID.randomUUID().toString();

  int count = 0;
  String userName = null;

  @PostConstruct
  void init() throws Exception {
    System.out.println("Session Bean constructed: " + name);
  }

  public int count() {
    System.out.println("Session Bean count: " + name);
    return count++;
  }

  public String getUserName() {
    System.out.println("Session Bean getUserName: " + name);
    return Optional.ofNullable(userName).orElseGet(() -> "Unknown User Name");
  }

  public void setUserName(String userName) {
    System.out.println("Session Bean setUserName: " + name);
    this.userName = Objects.requireNonNull(userName);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("count", count).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(count);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SessionBean other = (SessionBean) obj;
    return Objects.equals(this.count, other.count);
  }
}
