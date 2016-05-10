package jug.srp.better3;

import jug.srp.better.Database;

public class DbAddUserCommand {
	private final Database database;

	public DbAddUserCommand(Database database) {
		this.database = database;
	}
	
	public void execute(String email, String password) {
		database.execute("insert into user(email, password) values (?, ?)", email, password);
	}
}
