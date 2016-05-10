package jug.srp.better;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class UserManager {
	private final ConfirmationEmailSender confirmationEmailSender;
	private final UserRepository userRepository;

	public UserManager(UserRepository userRepository,
			ConfirmationEmailSender confirmationEmailSender) {
		super();
		this.userRepository = userRepository;
		this.confirmationEmailSender = confirmationEmailSender;
	}

	public User getUser(int userId) {
		return userRepository.getUser(userId);
	}

	public void registerUser(String email, String password) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(email) && !email.contains("@"));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password));

		userRepository.addUser(email, password);
		confirmationEmailSender.send(email);
	}

	public void deleteUser(int userId) {
		userRepository.deleteUser(userId);
	}
}
