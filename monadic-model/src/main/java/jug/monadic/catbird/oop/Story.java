package jug.monadic.catbird.oop;

public class Story {
	public static void main(String[] args) {
		Cat cat = new Cat();
		Bird bird = new Bird();

		cat.capture(bird);
		cat.eat();
	}
}
