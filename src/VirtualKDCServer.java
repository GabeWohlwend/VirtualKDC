import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class VirtualKDCServer {

	public static void main(String[] args){
		
		try
		{
			System.out.println("setting up server...");
			VirtualKDC kdc = new VirtualKDC();
			Registry registry;
			registry = LocateRegistry.createRegistry(4200);
			registry.rebind("VirtualKDCObject", kdc);
			System.out.println("Server successfully hosted...");
		} 
		catch (RemoteException e)
		{
			System.out.println("Error hosting VirtualKDC");
		} 	
	}
}