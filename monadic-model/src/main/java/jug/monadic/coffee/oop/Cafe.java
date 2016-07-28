package jug.monadic.coffee.oop;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

public class Cafe {
	public Coffee buyCoffee(CreditCard cc) {
		Coffee cup = new Coffee();
		cc.charge(cup.getPrice());
		return cup;
	}

	public List<Coffee> buyCoffees(CreditCard cc, int number) {
		return Stream.generate(() -> buyCoffee(cc))
				.limit(number)
				.collect(toList());
	}
}
