package jug.monadic.coffee.fp;

public class Charge {
	final long amount;
	final CreditCard cc;

	public Charge(CreditCard cc, long amount) {
		super();
		this.cc = cc;
		this.amount = amount;
	}

	public Charge add(Charge other) {
		if (cc == other.cc) {
			return new Charge(cc, amount + other.amount);

		} else {
			throw new IllegalArgumentException("different cc");
		}
	}
}
