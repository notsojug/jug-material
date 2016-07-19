package jug.monadic.catbird.oop;

public class Cat {
	private Bird bird;
	private boolean full;

	public void capture(Bird b) {
		bird = b;
	}

	public void eat() {
		full = true;
		bird = null;
	}
}
