package jug.monadic.coffee.fp2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Try;
import io.vavr.control.Try.Failure;

public class CafeTest {
	@Test
	public void shouldFailToBuyCoffeeWithoutAllowance() throws Exception {
		Tuple2<List<Coffee>, Charge> purchases = Cafe.buyCoffees(new CreditCard(1), 15);
		Charge charge = purchases._2;
		assertThat(charge.apply()).isInstanceOf(Failure.class);
	}

	@Test
	public void shouldFailToBuyCoffeeWithoutAllowanceToo() throws Exception {
		Tuple2<List<Coffee>, Charge> purchases = Cafe.buyCoffees(new CreditCard(1), 15);
		Tuple2<List<Coffee>, Try<CreditCard>> payed = purchases.map2(Charge::apply);
		assertThat(payed._2).isInstanceOf(Failure.class);
	}
}
