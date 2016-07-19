package jug.monadic.catbird.fp;

public class CatWithCatch {
	private final Bird bird;

	public CatWithCatch(Bird bird) {
		this.bird = bird;
	}
	
	public FullCat eat(){
		return new FullCat();
	}
}
