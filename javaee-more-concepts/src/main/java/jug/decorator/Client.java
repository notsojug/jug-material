package jug.decorator;

public interface Client {

	String getValueFromTheInternet(String id);

	String putValueToTheInternet(String id, String value);

}