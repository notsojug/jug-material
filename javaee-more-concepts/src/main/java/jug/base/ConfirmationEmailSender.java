package jug.base;

public class ConfirmationEmailSender {
	public void send(String email) {
		System.out.println("sending confirmation email to " + email);
		SmtpClient smtp = new SmtpClient();
		MailMessage message = MailMessage.of("noreply@website.com", email, "User registration confirmation", "...");
		smtp.send(message);
	}
}
