package jug.srp.better3;

public class UserFacade {
	private final RegisterUser registerUser;
	private final DbDeleteUserCommand dbDeleteUserCommand; 
	private final DbGetUserCommand dbGetUserCommand;

	public UserFacade(RegisterUser registerUser,
			DbDeleteUserCommand dbDeleteUserCommand, DbGetUserCommand dbGetUserCommand) {
		super();
		this.registerUser = registerUser;
		this.dbDeleteUserCommand = dbDeleteUserCommand;
		this.dbGetUserCommand = dbGetUserCommand;
	}

	public User getUser(int userId) {
		return dbGetUserCommand.execute(userId);
	}

	public void registerUser(String email, String password) {
		registerUser.registerUser(email, password);
	}

	public void deleteUser(int userId) {
		dbDeleteUserCommand.execute(userId);
	}
}
