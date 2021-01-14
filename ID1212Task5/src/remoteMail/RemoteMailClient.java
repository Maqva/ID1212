package remoteMail;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteMailClient {
	
	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.getRegistry((args.length >= 3)?args[2]:null);
			MailReader stub = (MailReader) registry.lookup("RemoteMail");
			System.out.println(stub.readMail(5, args[0], args[1]));
		} catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}
}
