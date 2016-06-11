package jug.streams;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

public class ListUtils {
	public static <T> List<T> filterList(List<T> original, Predicate<T> filter){
		Objects.requireNonNull(original);
		Objects.requireNonNull(filter);
		List<T> result = Lists.newArrayList();
		for(T one : original){
			if(filter.test(one)){
				result.add(one);
			}
		}
		return result;
	}
	
	public static <T, U> List<U> transformList(List<T> original, Function<T, U> function) {
		Objects.requireNonNull(original);
		Objects.requireNonNull(function);
		List<U> result = Lists.newArrayList();
		for (T one : original) {
			result.add(function.apply(one));
		}
		return result;
	}
}
