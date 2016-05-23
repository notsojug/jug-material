package jug.inject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class VeryUglyTest {
	@Test
	public void megaRecursiveCreationShouldBeVeryBoring() throws Exception {
		
		Database db = new Database();
		UserManager userManager = new UserManager(
				new ConfirmationEmailSender(), 
				new DbAddUserCommand(db),
				new DbDeleteUserCommand(db), 
				new DbGetUserCommand(db));

		User user = userManager.getUser(0);
		
		assertThat(user).as("it is very boring").isNull();
	}
}
