package jug.inject;

import javax.inject.Inject;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class UserManager {
	private final ConfirmationEmailSender confirmationEmailSender;
	private final DbAddUserCommand dbAddUserCommand; 
	private final DbDeleteUserCommand dbDeleteUserCommand; 
	private final DbGetUserCommand dbGetUserCommand;

	@Inject
	public UserManager(ConfirmationEmailSender confirmationEmailSender, DbAddUserCommand dbAddUserCommand,
			DbDeleteUserCommand dbDeleteUserCommand, DbGetUserCommand dbGetUserCommand) {
		super();
		this.confirmationEmailSender = confirmationEmailSender;
		this.dbAddUserCommand = dbAddUserCommand;
		this.dbDeleteUserCommand = dbDeleteUserCommand;
		this.dbGetUserCommand = dbGetUserCommand;
	}

	public User getUser(int userId) {
		return dbGetUserCommand.getUser(userId);
	}

	public void registerUser(String email, String password) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(email) && !email.contains("@"));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password));

		dbAddUserCommand.addUser(email, password);
		confirmationEmailSender.send(email);
	}

	public void deleteUser(int userId) {
		dbDeleteUserCommand.deleteUser(userId);
	}
}
