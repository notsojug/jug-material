package jug.monadic.oneclickbuy.fp1;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Story {
	public static void main(String[] args) {
		Function<List<Item>, Delivery> oneClickBuy = 
				((Function<List<Item>, Cart>) API::buy)
				.andThen(API::order)
				.andThen(API::deliver);

		Item phone = Item.of("Phone");
		Item watch = Item.of("Watch");
		Item book = Item.of("Book");
		Delivery delivery = oneClickBuy.apply(Arrays.asList(book, watch, phone));
		System.out.println(delivery);
	}
}
