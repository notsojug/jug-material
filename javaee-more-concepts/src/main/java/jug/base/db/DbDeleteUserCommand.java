package jug.base.db;

import javax.inject.Inject;

import jug.base.DeleteUserCommand;

public class DbDeleteUserCommand implements DeleteUserCommand {
	private final Database database;

	@Inject
	public DbDeleteUserCommand(Database database) {
		this.database = database;
	}
	
	@Override
	public void deleteUser(int userId) {
		database.execute("delete from user where userID=?", userId);
	}
}
