package jug.optional;

import java.util.Optional;

public class Person {

	private final Address address;

	public Person(Address address) {
		super();
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}
	
	public Optional<Address> address() {
		return Optional.ofNullable(address);
	}

	public static class Address {
		private final City city;

		public Address(City city) {
			super();
			this.city = city;
		}

		public City getCity() {
			return city;
		}

		public Optional<City> city() {
			return Optional.ofNullable(city);
		}
	}

	public static class City {
		private final String name;

		public City(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
		public Optional<String> name() {
			return Optional.ofNullable(name);
		}
	}
}
