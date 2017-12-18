package jug.trash;

import io.vavr.control.Try;

import static io.vavr.API.*;
import static io.vavr.Predicates.*;

import static io.vavr.Patterns.*;

public class Main {

	public static void main(String[] args) {
		// Old style
		try {
			Customer tooYoung = new Customer(16);
			Alcoholic alcoholic = buyAlcoholic(tooYoung);
		} catch (UnderageException ex) {
			// Troubles
		}

		// VAVR
		Try<Alcoholic> alcoholic = Try.of(() -> buyAlcoholic(new Customer(16)));
		Try<Integer> concentration = alcoholic.map(Alcoholic::getAlcoholConcentration);
		concentration.filter(i -> i > 0);
		String result = Match(concentration).of(
				Case($Success($()), c -> "drinked " + c),
				Case($Failure($()), t -> "troubles"));
		Try<Integer> recovered = concentration.recover(th -> Match(th).of(
				Case($(instanceOf(UnderageException.class)), () -> 0),
				Case($(), () -> -1)));
	}

	static Alcoholic buyAlcoholic(Customer customer) throws UnderageException {
		if (customer.getAge() < 18) {
			throw new UnderageException("Age: " + customer.getAge());
		}
		return new Alcoholic(5);
	}
}
