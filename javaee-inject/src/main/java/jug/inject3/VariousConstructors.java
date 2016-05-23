package jug.inject3;

public class VariousConstructors {
	private final String inner;

	public VariousConstructors(String inner) {
		super();
		this.inner = inner;
	}
	
	public VariousConstructors(){
		inner = "my useless default inner value";
	}
	
	public String getInner() {
		return inner;
	}
}
