package remoteMail;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MailReader extends Remote{
	public String readMail(int index, String userName, String passWord) throws RemoteException;
}
