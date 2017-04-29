import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.Key;


public interface VirtualKDCInterface extends Remote
{
	public void addClient(String name, Key key) throws RemoteException;
	public Key getKeyFromClientName(String name) throws RemoteException;
	public boolean nameExists(String name) throws RemoteException;
	public String clientNamesToString() throws RemoteException;
	
}