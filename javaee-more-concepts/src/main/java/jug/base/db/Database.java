package jug.base.db;

import javax.enterprise.context.ApplicationScoped;

import com.google.common.base.Joiner;

@ApplicationScoped
public class Database {

	public <T> T querySingle(Class<T> clazz, String query, Object... params) {
		System.out.println("Running query <" + query + "> with params " + Joiner.on(", ").join(params));
		return null;
	}

	public void execute(String query, Object... params) {
		System.out.println("Executing query <" + query + "> with params " + Joiner.on(", ").join(params));
	}

}
