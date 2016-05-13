package jug.inject;

import javax.inject.Inject;

public class DbGetUserCommand {
	private final Database database;

	@Inject
	public DbGetUserCommand(Database database) {
		this.database = database;
	}
	
	public User getUser(int userId) {
		return database.querySingle(User.class, "Select * from users where userId=?", userId);
	}
}
