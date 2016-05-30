package jug.interceptor;

public class ClassToBeLogged {

	@Logged
	public String someMethod(String aParam, int anotherParam) {
		return "been there, done that";
	}
}
