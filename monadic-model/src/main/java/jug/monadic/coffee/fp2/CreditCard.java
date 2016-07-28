package jug.monadic.coffee.fp2;

import com.google.common.base.Preconditions;

public class CreditCard {
	final long allowance;

	public CreditCard(long allowance) {
		super();
		Preconditions.checkArgument(allowance >= 0, "Cannot have less than 0 allowance");
		this.allowance = allowance;
	}
	
	public CreditCard pay(long amount){
		return new CreditCard(allowance-amount);
	}
}
