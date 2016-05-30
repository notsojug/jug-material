package jug.base.db;

import javax.inject.Inject;

import jug.base.AddUserCommand;

public class DbAddUserCommand implements AddUserCommand {
	private final Database database;

	@Inject
	public DbAddUserCommand(Database database) {
		this.database = database;
	}
	
	@Override
	public int addUser(String email, String password) {
		database.execute("insert into user(email, password) values (?, ?)", email, password);
		return 0;
	}
}
