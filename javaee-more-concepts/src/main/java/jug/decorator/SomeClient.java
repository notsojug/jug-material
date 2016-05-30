package jug.decorator;

public class SomeClient implements Client {
	@Override
	public String getValueFromTheInternet(String id){
		return "42"; 
	}

	@Override
	public String putValueToTheInternet(String id, String value){
		return "done";
	}
}
