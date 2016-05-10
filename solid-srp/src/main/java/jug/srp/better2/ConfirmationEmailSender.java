package jug.srp.better2;

public class ConfirmationEmailSender {
	public void send(String email) {
		SmtpClient smtp = new SmtpClient();
		MailMessage message = MailMessage.of("noreply@website.com", email, "User registration confirmation", "...");
		smtp.send(message);
	}
}
