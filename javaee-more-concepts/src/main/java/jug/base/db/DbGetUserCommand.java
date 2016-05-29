package jug.base.db;

import javax.inject.Inject;

import jug.base.GetUserCommand;
import jug.base.User;

public class DbGetUserCommand implements GetUserCommand {
	private final Database database;

	@Inject
	public DbGetUserCommand(Database database) {
		this.database = database;
	}
	
	@Override
	public User getUser(int userId) {
		return database.querySingle(User.class, "Select * from users where userId=?", userId);
	}
}
