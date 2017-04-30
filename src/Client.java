import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Client
{
	private static boolean serverConnected = false;
	private static boolean clientExists = false;

	
	private static Scanner keyboardIn = new Scanner(System.in);	//scanner for user input
	private static String serverIP = "";						//string value for the IP of the server
	private static String clientName = "";						//string value for this client's name
	private static String otherClient = "";						//string value for the other clients name
	private static Key publicKey;								//this client's public key
	private static Key privateKey;								//this client's private key
	private static Key otherPublic;								//the other client's public key
	private static VirtualKDCInterface kdc;						//the RMI object
	private static Registry reg;								//the RMI registry object
	private static KeyPairGenerator kpg;						//this object generates key pairs
	private static String message;								//this is the message to send to the other client
	
	public static void main(String[] args){
		
		//get the IP of the server from the user
		System.out.println("Enter IP of server");
		serverIP = keyboardIn.nextLine();
		
		//attempt to connect to RMI server
		try
		{
			reg = LocateRegistry.getRegistry(serverIP, 4200);
			kdc = (VirtualKDCInterface) reg.lookup("VirtualKDCObject");
			serverConnected = true;			
		}
		catch (RemoteException e){
			serverConnected = false;
		}
		catch (NotBoundException e){
			serverConnected = false;
		}
		
		//close program if server could not be reached
		if(!serverConnected){
			System.out.println("Error connecting. Please restart client.");
			System.exit(0);
		}
		
		
		//prompt user for their name
		System.out.println("Enter your name");
		clientName = keyboardIn.nextLine().toLowerCase();
		
		
		
		/*
		 * attempt to get an instance of the RSA Key generator with a given method 
		 * (this needs a try catch in case it's given an algorithm that's not valid)
		 */
		try{  kpg = KeyPairGenerator.getInstance("RSA");  } catch (NoSuchAlgorithmException e1){printError();}
		
		//create keys and store them in the variables created globally
		kpg.initialize(2048);
		KeyPair kp = kpg.genKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
		
		//add your client's info to the KDC
		try{  kdc.addClient(clientName, publicKey);  }catch (RemoteException e){}
		
		
		/*
		 * prompt user for the other client you'd like to talk to and repeat this process until 
		 * they find one that is stored on the kdc
		 */
		
		do{
			System.out.println("Enter the name of the client you'd like to talk to");
			otherClient = keyboardIn.nextLine().toLowerCase();
			
			//check the kdc object to see if the client exists 
			try{  
				clientExists = kdc.nameExists(otherClient);
				//if it does exist, set otherPublic to the key you get from the kdc
				if(clientExists){  
					otherPublic = kdc.getKeyFromClientName(otherClient);  
				}
				else{
					System.out.println("This client isn't connected right now");
				}
			}catch (RemoteException e){printError();}
		}while(!clientExists);
		
		//prompt for message you'd like to send the other client
		System.out.println("client key retrieved");
		
		System.out.println("What is the message you'd like to send to " + otherClient + "? ");
		message = keyboardIn.nextLine();

		
		/* TO-DO:
		 * implement chat functionality. This should be as simple as recycling a basic socket based chat
		 * app but instead of sending a plaintext string, send a string encrypted with the java RSA asymmetric
		 * encryption. The key distribution part is finished and you shouldn't have to mess with any RMI stuff
		 * Let me know what issues you have or if you have any questions
		 */
		
		


	}

	private static void printError()
	{
		System.out.println("SOMETHING DIDN'T WORK");

	}
	
}
