package jug.srp.bad;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class UserManager {
	private final Database database;

	public UserManager(Database database) {
		super();
		this.database = database;
	}

	public User getUser(int userId) {
		return database.querySingle(User.class, "Select * from users where userId=?", userId);
	}

	public void registerUser(String email, String password) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(email) && !email.contains("@"));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password));

		database.execute("insert into user(email, password) values (?, ?)", email, password);

		SmtpClient smtp = new SmtpClient();
		MailMessage message = MailMessage.of("noreply@website.com", email, "User registration confirmation", "...");
		smtp.send(message);
	}

	public void deleteUser(int userId) {
		database.execute("delete from user where userID=?", userId);
	}
}
