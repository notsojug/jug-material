package jug.guava;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class State {

  private final String stateCode;
  private final String name;
  private final String regionCode;
  private final int population;

  public State(String stateCode, String name, String regionCode, int population) {
    super();
    this.stateCode = stateCode;
    this.name = name;
    this.regionCode = regionCode;
    this.population = population;
  }

  public String getStateCode() {
    return stateCode;
  }

  public String getName() {
    return name;
  }

  public String getRegionCode() {
    return regionCode;
  }

  public int getPopulation() {
    return population;
  }

	/**
	 * This method honor the same contract with {@link Object#hashCode()},
	 * but uses the guava {@link Objects#hashCode()} to calculate the
	 * value. 
	 * <p>
	 * The calculated value may be different from eclipse-generated
	 * algorithm, but it is guaranteed to be compliant with java specification.
	 * 
	 * @return the hashCode of this object.
	 */
  public int hashCodeGuava() {
    return Objects
        .hashCode(this.stateCode,
            this.name,
            this.regionCode,
            this.population);
  }
  
	/**
	 * This method satisfies the contract for {@link Object#equals(Object)}
	 * , but using the {@link Objects#equals(Object)} utility to calculate
	 * equality.
	 * <p>
	 * While this may be not an improvement in performance or functionality, it
	 * is indeed more compact and clear than the naive implementation from
	 * eclipse.
	 * 
	 * @param obj
	 *            the object to compare against.
	 * @return true if the given object is equal to the instance.
	 */
  public boolean equalsGuava(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final State other = (State) obj;
    return Objects.equal(this.stateCode, other.stateCode)
        && Objects.equal(this.name, other.name)
        && Objects.equal(this.regionCode, other.regionCode)
        && Objects.equal(this.population, other.population);
  }
  
  
	/**
	 * Same as {@link Object#toString()} from eclipse, but using the
	 * {@link MoreObjects.ToStringHelper} class.
	 * <p>
	 * The ToStringHelper class simplifies/clarifies the creation of the
	 * toString method with a handy facility to omit null values. Similarly to
	 * the {@link #equalsGuava(Object)}, this approach doesn't change the
	 * outcome of the method, it just clarifies the intent in a readable way.
	 * 
	 * @return the string representation of the instance.
	 */
  public String toStringGuava() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("stateCode", stateCode)
        .add("name", name)
        .toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + population;
    result = prime * result + ((regionCode == null) ? 0 : regionCode.hashCode());
    result = prime * result + ((stateCode == null) ? 0 : stateCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof State)) {
      return false;
    }
    State other = (State) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (population != other.population) {
      return false;
    }
    if (regionCode == null) {
      if (other.regionCode != null) {
        return false;
      }
    } else if (!regionCode.equals(other.regionCode)) {
      return false;
    }
    if (stateCode == null) {
      if (other.stateCode != null) {
        return false;
      }
    } else if (!stateCode.equals(other.stateCode)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "State [" + (stateCode != null ? "stateCode=" + stateCode + ", " : "")
        + (name != null ? "name=" + name : "") + "]";
  }
}