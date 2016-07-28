package jug.monadic.coffee.fp;

import java.util.function.Function;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Stream;

public class Cafe {
	public Tuple2<Coffee, Charge> buyCoffee(CreditCard cc) {
		Coffee cup = new Coffee();
		return Tuple.of(cup, new Charge(cc, cup.getPrice()));
	}

	public Tuple2<List<Coffee>, Charge> buyCoffees(CreditCard cc, int number) {
		Stream<Tuple2<Coffee, Charge>> tupledPurchases = Stream.continually(() -> buyCoffee(cc))
				.take(number);
		Tuple2<Stream<Coffee>, Stream<Charge>> purchases = tupledPurchases
				.unzip(Function.identity());
		return purchases.map(Stream::toList, charges -> charges.reduce(Charge::add));
	}
}
