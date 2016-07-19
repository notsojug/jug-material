package jug.monadic.catbird.fp;

public class Cat {
	public CatWithCatch capture(Bird b){
		return new CatWithCatch(b);
	}
}
