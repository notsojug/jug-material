package jug.base;

import java.util.Optional;

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

	public Optional<User> getUser(int userId) {
		return Optional.ofNullable(getUserCommand.getUser(userId));
	}

	public int registerUser(String email, String password) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(email) && email.contains("@"));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password));

		int userId = addUserCommand.addUser(email, password);
		confirmationEmailSender.send(email);
		return userId;
	}

	public void deleteUser(int userId) {
		deleteUserCommand.deleteUser(userId);
	}
}
