package jug.inject;

public class SmtpClient {

	public void send(MailMessageInterface message) {
		System.out.println("Sending message " + message);
	}

}
