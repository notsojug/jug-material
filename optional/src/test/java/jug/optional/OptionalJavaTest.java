package jug.optional;

import static org.assertj.core.api.Assertions.*;

import java.io.Serializable;
import java.util.Optional;

import org.junit.Test;

import jug.optional.Person.Address;
import jug.optional.Person.City;

public class OptionalJavaTest {

	@Test
	public void shouldNotBeSerializable() throws Exception {
		assertThat(Optional.class.getInterfaces()).doesNotContain(Serializable.class);
	}

	@Test
	public void testOldJavaStyle() throws Exception {
		Person person = new Person(null);
		if (person != null) {
			Address address = person.getAddress();
			assertThat(address).isNull();
			if (address != null) {
				City city = address.getCity();
				if (city != null) {
					city.getName();
				}
			}
		}
	}

	@Test
	public void testWrongOptional() throws Exception {
		Person person = new Person(null);
		if (person != null) {
			Optional<Address> address = person.address();
			assertThat(address).isEmpty();
			if (address.isPresent()) {
				Optional<City> city = address.get().city();
				if (city.isPresent()) {
					city.get().name();
				}
			}
		}
	}

	@Test
	public void testOptionalCorrectPresent() throws Exception {
		Optional<Person> person = Optional.of(new Person(new Address(new City("Padua"))));
		
		String theCity = person.flatMap(p -> p.address())
				.flatMap(a -> a.city())
				.flatMap(c -> c.name())
				.orElse("Unknow");
		
		assertThat(theCity).isNotNull().isEqualTo("Padua");
	}

	@Test
	public void testOptionalCorrectNoCity() throws Exception {
		Optional<Person> lostPerson = Optional.of(new Person(new Address(null)));
		
		String theUnknowCity = lostPerson.flatMap(p -> p.address())
				.flatMap(a -> a.city())
				.flatMap(c -> c.name())
				.orElse("Unknow");
			
		assertThat(theUnknowCity).isNotNull().isEqualTo("Unknow");
	}

	@Test
	public void testOptionalCorrectNoAddress() throws Exception {
		Optional<Person> lostPerson = Optional.of(new Person(null));
		
		String theUnknowCity = lostPerson.flatMap(p -> p.address())
				.flatMap(a -> a.city())
				.flatMap(c -> c.name())
				.orElse("Unknow");
			
		assertThat(theUnknowCity).isNotNull().isEqualTo("Unknow");
	}

	@Test
	public void testOptionalCorrectNoPerson() throws Exception {
		Optional<Person> lostPerson = Optional.empty();
		
		String theUnknowCity = lostPerson.flatMap(p -> p.address())
				.flatMap(a -> a.city())
				.flatMap(c -> c.name())
				.orElse("Unknow");
			
		assertThat(theUnknowCity).isNotNull().isEqualTo("Unknow");
	}

	@Test
	public void testOptionalBetter() throws Exception {
		Optional<Person> person = Optional.of(new Person(new Address(new City("Padua"))));
		
		String theCity = person.flatMap(Person::address)
				.flatMap(Address::city)
				.flatMap(City::name)
				.orElse("Unknow");
		
		assertThat(theCity).isNotNull().isEqualTo("Padua");
	}
	
	@Test
	public void testOptMap() throws Exception {
		OptMap<String, Person> population = new OptMap<>();
		population.put("joshua", new Person(new Address(new City("Padua"))));
		
		String joshuaCity = population.find("joshua")
				.flatMap(Person::address)
				.flatMap(Address::city)
				.flatMap(City::name)
				.orElse("Unknow");
		
		assertThat(joshuaCity).isNotNull().isEqualTo("Padua");
		
		String theUnknowCity = population.find("roman")
				.flatMap(Person::address)
				.flatMap(Address::city)
				.flatMap(City::name)
				.orElse("Unknow");
		
		assertThat(theUnknowCity).isNotNull().isEqualTo("Unknow");
	}
}
