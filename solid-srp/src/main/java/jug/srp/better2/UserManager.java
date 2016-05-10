package jug.srp.better2;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class UserManager {
	private final ConfirmationEmailSender confirmationEmailSender;
	private final DbAddUserCommand dbAddUserCommand; 
	private final DbDeleteUserCommand dbDeleteUserCommand; 
	private final DbGetUserCommand dbGetUserCommand;

	public UserManager(ConfirmationEmailSender confirmationEmailSender, DbAddUserCommand dbAddUserCommand,
			DbDeleteUserCommand dbDeleteUserCommand, DbGetUserCommand dbGetUserCommand) {
		super();
		this.confirmationEmailSender = confirmationEmailSender;
		this.dbAddUserCommand = dbAddUserCommand;
		this.dbDeleteUserCommand = dbDeleteUserCommand;
		this.dbGetUserCommand = dbGetUserCommand;
	}

	public User getUser(int userId) {
		return dbGetUserCommand.execute(userId);
	}

	public void registerUser(String email, String password) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(email) && !email.contains("@"));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password));

		dbAddUserCommand.execute(email, password);
		confirmationEmailSender.send(email);
	}

	public void deleteUser(int userId) {
		dbDeleteUserCommand.execute(userId);
	}
}
