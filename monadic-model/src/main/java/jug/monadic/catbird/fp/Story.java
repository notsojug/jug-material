package jug.monadic.catbird.fp;

import java.util.function.BiFunction;

public class Story {
	public static void main(String[] args) {
		BiFunction<Cat, Bird, FullCat> story = 
				((BiFunction<Cat, Bird, CatWithCatch>)Cat::capture)
				.andThen(CatWithCatch::eat);
		
		FullCat fullCat = story.apply(new Cat(), new Bird());
	}
	
	
	
	
	
	
	// a better alternative:
	
	static <T,U,R> BiFunction<T, U, R> bifunction(BiFunction<T, U, R> f){
		return f;
	}
	
	public static void main2(){
		BiFunction<Cat, Bird, FullCat> story = bifunction(Cat::capture).andThen(CatWithCatch::eat);
	}
}
