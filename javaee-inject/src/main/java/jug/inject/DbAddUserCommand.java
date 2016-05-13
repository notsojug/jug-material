package jug.inject;

import javax.inject.Inject;

public class DbAddUserCommand {
	private final Database database;

	@Inject
	public DbAddUserCommand(Database database) {
		this.database = database;
	}
	
	public void addUser(String email, String password) {
		database.execute("insert into user(email, password) values (?, ?)", email, password);
	}
}
