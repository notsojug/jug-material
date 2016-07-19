package jug.monadic.oneclickbuy.fp1;

import java.util.List;

public class API {
	public static Cart buy(List<Item> items) {
		return ImmutableCart.of(items);
	}
	
	public static Order order(Cart cart){
		return ImmutableOrder.of(cart.items(), "Some default address");
	}
	
	public static Delivery deliver(Order order){
		return ImmutableDelivery.of(order.items(), order.address(), "Just dispatched");
	}
}
