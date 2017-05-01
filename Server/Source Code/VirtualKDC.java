import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;


public class VirtualKDC extends UnicastRemoteObject implements VirtualKDCInterface{

	
	private static final long serialVersionUID = 1L;
	private ArrayList<StoredClient> clients = new ArrayList<StoredClient>();
	
	VirtualKDC() throws RemoteException{
		
	}
	
	@Override
	public void addClient(String name, Key key) throws RemoteException {
		StoredClient tempClient = new StoredClient(name, key);
		clients.add(tempClient);
		clients.trimToSize();
	}
	
	@Override
	public Key getKeyFromClientName(String name) throws RemoteException {
		Key tempKey = null;
		int index = getIndexOfName(name);
		tempKey = clients.get(index).getPubKey();
		return tempKey;
	}
	
	private int getIndexOfName(String name) throws RemoteException {
		int index = -1;
		for(int i = 0; i < clients.size(); i++){
			if(clients.get(i).getName().equals(name)){ 
				index = i;
			}
		}
		return index;
	}
	
	@Override
	public boolean nameExists(String name) throws RemoteException {
		if(getIndexOfName(name) == -1){
			return false;
		}
		else{
			return true;
		}
	}
	
	@Override
	public String clientNamesToString() throws RemoteException{
		String tempString = "";
		for(int i = 0; i < clients.size(); i++){
			tempString += clients.get(i).getName() + "\n";
		}
		return tempString;
	}
	
	public byte[] getClientMessage(String name) throws RemoteException{
		byte[] message = clients.get(getIndexOfName(name)).getMessage();
		
		return message;
	}
	
	public void setClientMessage(String name, byte[] message) throws RemoteException{
		clients.get(getIndexOfName(name)).setMessage(message);
	}
	
}