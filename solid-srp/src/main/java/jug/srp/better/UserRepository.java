package jug.srp.better;

public class UserRepository {

	private final Database database;

	public UserRepository(Database database) {
		this.database = database;
	}

	public User getUser(int userId) {
		return database.querySingle(User.class, "Select * from users where userId=?", userId);
	}

	public void addUser(String email, String password) {
		database.execute("insert into user(email, password) values (?, ?)", email, password);
	}

	public void deleteUser(int userId) {
		database.execute("delete from user where userID=?", userId);
	}
}
