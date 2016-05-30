package jug.alternative.memory;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import com.google.common.collect.Maps;

import jug.base.AddUserCommand;
import jug.base.DeleteUserCommand;
import jug.base.GetUserCommand;
import jug.base.User;


@ApplicationScoped
@Alternative
public class InMemoryUserRepo implements AddUserCommand, DeleteUserCommand, GetUserCommand {

	Map<Integer, User> users = Maps.newHashMap();

	@Override
	public User getUser(int userId) {
		return users.get(userId);
	}

	@Override
	public void deleteUser(int userId) {
		User old = users.remove(userId);
		System.out.println("Old was " + old);
	}

	@Override
	public int addUser(String email, String password) {
		User user = User.of(newId(), email, "" + password.hashCode());
		users.put(user.userId(), user);
		return user.userId();
	}

	private int newId() {
		return users.size() + 1;
	}

}
