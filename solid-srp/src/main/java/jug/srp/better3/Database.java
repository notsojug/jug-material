package jug.srp.better3;

import com.google.common.base.Joiner;

public class Database {

	public <T> T querySingle(Class<T> clazz, String query, Object... params) {
		System.out.println("Running query <" + query + "> with params " + Joiner.on(", ").join(params));
		return null;
	}

	public void execute(String query, Object... params) {
		System.out.println("Executing query <" + query + "> with params " + Joiner.on(", ").join(params));
	}

}
