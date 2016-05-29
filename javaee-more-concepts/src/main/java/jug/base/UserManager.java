package jug.base;

import javax.inject.Inject;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class UserManager {
	private final ConfirmationEmailSender confirmationEmailSender;
	private final AddUserCommand addUserCommand; 
	private final DeleteUserCommand deleteUserCommand; 
	private final GetUserCommand getUserCommand;

	@Inject
	public UserManager(ConfirmationEmailSender confirmationEmailSender, AddUserCommand dbAddUserCommand,
			DeleteUserCommand dbDeleteUserCommand, GetUserCommand dbGetUserCommand) {
		super();
		this.confirmationEmailSender = confirmationEmailSender;
		this.addUserCommand = dbAddUserCommand;
		this.deleteUserCommand = dbDeleteUserCommand;
		this.getUserCommand = dbGetUserCommand;
	}

	public User getUser(int userId) {
		return getUserCommand.getUser(userId);
	}

	public void registerUser(String email, String password) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(email) && !email.contains("@"));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password));

		addUserCommand.addUser(email, password);
		confirmationEmailSender.send(email);
	}

	public void deleteUser(int userId) {
		deleteUserCommand.deleteUser(userId);
	}
}
