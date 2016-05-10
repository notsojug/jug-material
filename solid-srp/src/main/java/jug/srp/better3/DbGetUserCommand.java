package jug.srp.better3;

import jug.srp.better.Database;

public class DbGetUserCommand {
	private final Database database;

	public DbGetUserCommand(Database database) {
		this.database = database;
	}
	
	public User execute(int userId) {
		return database.querySingle(User.class, "Select * from users where userId=?", userId);
	}
}
