package jug.inject2;

import com.google.common.base.Preconditions;

public class SomethingWithLotsOfConfiguration {

	private final String path;
	
	private SomethingWithLotsOfConfiguration(String configPath){
		this.path=configPath;
	}
	
	public static SomethingWithLotsOfConfiguration create(String configPath){
		return new SomethingWithLotsOfConfiguration(Preconditions.checkNotNull(configPath));
	}
	
	public void init(){
		// very long initialization
		System.out.println(path);
	}
}
