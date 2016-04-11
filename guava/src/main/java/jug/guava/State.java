package jug.guava;

import com.google.common.base.MoreObjects;

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

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).omitNullValues().add("stateCode", stateCode).add("name", name)
				.toString();
	}
}