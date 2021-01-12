package remoteMail;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class RemoteMailServer implements MailReader{
	public RemoteMailServer() {
		super();
	}

	public static void main(String[] args) {
		try {
			RemoteMailServer server = new RemoteMailServer();
			MailReader stub = (MailReader) UnicastRemoteObject.exportObject(server, 0);
			
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("RemoteMail", stub);
			
			System.out.println("Server Started...");
		} catch (RemoteException e) {
			System.err.println("A remote exception has occured.");
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			System.err.println("The Registry name is already bound.");
		}
	}
	
	public String readMail(int index, String userName, String passWord){
		try {
			Properties props = new Properties();
			props.setProperty("mail.store.protocol", "imaps");
			Session emailSession = Session.getDefaultInstance(props);
			Store emailStore = emailSession.getStore("imaps");
			emailStore.connect("webmail.kth.se",userName, passWord);
			Folder emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			Message[] messages = emailFolder.getMessages();
			Message m = messages[index];
			String output = "";
			output += "From: "+m.getFrom()[0];
			output += "\nSubject: "+m.getSubject();
			output += "\nSent: "+m.getSentDate();
			Object o = m.getContent();
			if(o instanceof String)
				output += "\nBody: "+(String)o;
			return output;
		} catch (MessagingException e) {
			return"A MessagingException occured.";
		} catch (IOException e) {
			return"A IOException has occured.";
		}
		
	}
}
