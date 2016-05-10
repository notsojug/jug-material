package jug.srp.better3;

import jug.srp.better.Database;

public class DbDeleteUserCommand {
	private final Database database;

	public DbDeleteUserCommand(Database database) {
		this.database = database;
	}
	
	public void execute(int userId) {
		database.execute("delete from user where userID=?", userId);
	}
}
