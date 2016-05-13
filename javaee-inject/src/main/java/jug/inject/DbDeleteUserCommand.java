package jug.inject;

import javax.inject.Inject;

public class DbDeleteUserCommand {
	private final Database database;

	@Inject
	public DbDeleteUserCommand(Database database) {
		this.database = database;
	}
	
	public void deleteUser(int userId) {
		database.execute("delete from user where userID=?", userId);
	}
}
