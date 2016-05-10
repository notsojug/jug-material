package jug.srp.better3;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class RegisterUser {
	private final ConfirmationEmailSender confirmationEmailSender;
	private final DbAddUserCommand dbAddUserCommand;

	public RegisterUser(ConfirmationEmailSender confirmationEmailSender, DbAddUserCommand dbAddUserCommand) {
		this.confirmationEmailSender = confirmationEmailSender;
		this.dbAddUserCommand = dbAddUserCommand;
	}

	public void registerUser(String email, String password) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(email) && !email.contains("@"));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password));

		dbAddUserCommand.execute(email, password);
		confirmationEmailSender.send(email);
	}
}
