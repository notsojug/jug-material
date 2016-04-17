package jug.optional;

import static org.assertj.core.api.Assertions.*;

import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.Test;

import jug.optional.Person.Address;
import jug.optional.Person.City;

public class OptionalJavaTest {

	@Test
	public void shouldNotBeSerializable() throws Exception {
		assertThat(Optional.class.getInterfaces()).doesNotContain(Serializable.class);
	}
	
	@Test
	public void shouldSupplyAndConsumeAndThrow() throws Exception {
		Supplier<String> supplier = () -> "default";
		Supplier<Exception> exceptionSupplier = () -> new IllegalArgumentException();
		Consumer<String> consumer = s -> System.out.println(s);
		
		// What if consumers and suppliers are external?
		Optional.ofNullable("test").orElseGet(supplier);
		Optional.ofNullable("test").orElseThrow(exceptionSupplier);
		Optional.ofNullable("test").ifPresent(consumer);
		
		Optional.ofNullable("test").orElseGet(() -> "default");
		Optional.ofNullable("test").orElseThrow(() -> new IllegalArgumentException());
//		Optional.ofNullable("test").orElseThrow(IllegalArgumentException::new);
		Optional.ofNullable("test").ifPresent(s -> System.out.println(s));
	}
	
	@Test
	public void shouldFindOnlyPositiveValues_shootFoot() throws Exception {
		Properties props = new Properties();
		props.setProperty("a", "5");
		props.setProperty("b", "true");
		props.setProperty("c", "-3");
		
		Integer ret = 0; // Default value
		String value = props.getProperty("b"); // Prolly null
		if (value != null) {
			try {
				int i = Integer.valueOf(value); // Exception!
				if (i > 0) { // The true logic
					ret = i;
				}
			} catch (NumberFormatException e) {}
		}
		
		assertThat(ret).isEqualTo(0);
	}
	
	@Test
	public void shouldFindOnlyPositiveValues() throws Exception {
		Properties props = new Properties();
		props.setProperty("a", "5");
		props.setProperty("b", "true");
		props.setProperty("c", "-3");
		
		// Final and no exceptions!
		final Integer ret = 
				Optional.of(props.getProperty("b")) // Optional String
				.flatMap(OptionalUtility::stringToInt) // To optional integer
				.filter(i -> i > 0) // The logic
				.orElse(0); // Default value
		
		assertThat(ret).isEqualTo(0);
	}

	@Test
	public void testOldJavaStyle() throws Exception {
		Person person = new Person(null);
		if (person != null) { // Prolly null
			Address address = person.getAddress();
			assertThat(address).isNull();
			if (address != null) { // Prolly null
				City city = address.getCity();
				if (city != null) { // Prolly null
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
			if (address.isPresent()) { // Ugly check
				Optional<City> city = address.get().city();
				if (city.isPresent()) { // Ugly check
					city.get().name(); // NoSuchElementException is close here
				}
			}
		}
	}

	@Test
	public void testOptionalCorrectPresent() throws Exception {
		Optional<Person> person = Optional.of(new Person(new Address(new City("Padua"))));
		
		String theCity = person.flatMap(p -> p.address()) // Optional person to Optional address
				.flatMap(a -> a.city()) // Optional address to optional city
				.flatMap(c -> c.name()) // Optional city to optional string
				.orElse("Unknow"); // Default value
		
		assertThat(theCity).isNotNull().isEqualTo("Padua");
	}

	@Test
	public void testOptionalCorrectNoCity() throws Exception {
		Optional<Person> lostPerson = Optional.of(new Person(new Address(null)));
		
		// Same code multiple scenarios
		String theUnknowCity = lostPerson.flatMap(p -> p.address())
				.flatMap(a -> a.city())
				.flatMap(c -> c.name())
				.orElse("Unknow");
			
		assertThat(theUnknowCity).isNotNull().isEqualTo("Unknow");
	}

	@Test
	public void testOptionalCorrectNoAddress() throws Exception {
		Optional<Person> lostPerson = Optional.of(new Person(null));

		// Same code multiple scenarios
		String theUnknowCity = lostPerson.flatMap(p -> p.address())
				.flatMap(a -> a.city())
				.flatMap(c -> c.name())
				.orElse("Unknow");
			
		assertThat(theUnknowCity).isNotNull().isEqualTo("Unknow");
	}

	@Test
	public void testOptionalCorrectNoPerson() throws Exception {
		Optional<Person> lostPerson = Optional.empty();

		// Same code multiple scenarios
		String theUnknowCity = lostPerson.flatMap(p -> p.address())
				.flatMap(a -> a.city())
				.flatMap(c -> c.name())
				.orElse("Unknow");
			
		assertThat(theUnknowCity).isNotNull().isEqualTo("Unknow");
	}

	@Test
	public void testOptionalBetter() throws Exception {
		Optional<Person> person = Optional.of(new Person(new Address(new City("Padua"))));
		
		// Why use a lambda when we already have a method that does the same?
		// Method references ROCKS
		String theCity = person.flatMap(Person::address)
				.flatMap(Address::city)
				.flatMap(City::name)
				.orElse("Unknow");
		
		assertThat(theCity).isNotNull().isEqualTo("Padua");
	}
	
	@Test
	public void testOptMap() throws Exception {
		// What if all java methods that can return null where returning optional instead??
		// Unfortunately this breaks backward compatibility... :(
		
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
