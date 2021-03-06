package mailClient;

public class Main {
	private static final String receivingServerAddress = "webmail.kth.se";
	private static final int receivingServerPort = 993;
	private static final String sendingServerAddress = "smtp.kth.se";
	private static final int sendingServerPort = 587;
	
	/**
	 * 
	 * @param args String array of arguments, 0 = email username, 1 = email password, 2 = public ipaddress, 3 = email address 
	 */
	public static void main(String[] args) {
		IMAPSSLClient readingClient;
		SMTPSTARTSSLClient writingClient;
		readingClient = new IMAPSSLClient(receivingServerAddress, receivingServerPort);
		readingClient.readInbox(args[0], args[1]);
		writingClient = new SMTPSTARTSSLClient(sendingServerAddress, sendingServerPort, args[2], args[3]);
		writingClient.writeTestEmail(args[0], args[1]);
	}

}
