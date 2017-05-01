import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.Key;

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
	private static boolean isAMessage = false;					//checks if there is a message
	
	
	public static void main(String[] args) throws InterruptedException, RemoteException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		
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
		
		System.out.println("\nUser added. Public key of " + clientName + " is: " + publicKey.toString() + "\n");
		

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
					System.out.println("Public key of " + otherClient + " retrieved: " + otherPublic.toString());

				}
				else{
					System.out.println("This client isn't connected right now");
				}
			}catch (RemoteException e){printError();}
		}while(!clientExists);
		
		//prompt for message you'd like to send the other client		
		System.out.println("\nWhat is the message you'd like to send to " + otherClient + "? ");
		message = keyboardIn.nextLine();


		
		
		//Gets the message in bytes to encrypt
		byte [] encryptedmessage = message.getBytes(Charset.forName("UTF-8"));
		
		
		//Tries to Encrypt the message
		try {
			try {
				encryptedmessage = encrypt(otherPublic, encryptedmessage);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		try {
			kdc.setClientMessage(otherClient, encryptedmessage);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		do{
			byte [] receivedMessage = kdc.getClientMessage(clientName);
			
			if(Arrays.equals(receivedMessage, "".getBytes(Charset.forName("UTF-8")))){
				isAMessage = false;
				Thread.sleep(150);
			}
			else{
				
				isAMessage = true;
				receivedMessage = decrypt(privateKey, receivedMessage);
				
				String receivedMessageString = new String(receivedMessage, "UTF8");
				System.out.println("\nRecieved Message is: " + receivedMessageString);
			}			
			
		}while(!isAMessage);
		
		
		
	}

	private static byte[] encrypt(Key otherPublic2, byte[] encryptedmessage) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
		
		cipher.init(Cipher.ENCRYPT_MODE, otherPublic2);
		
		return cipher.doFinal(encryptedmessage);
		
	}

	private static byte[] decrypt (Key key, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
		
		cipher.init(Cipher.DECRYPT_MODE,  key);
		
		return cipher.doFinal(ciphertext);
	}
	
	
	private static void printError()
	{
		System.out.println("SOMETHING DIDN'T WORK");

	}
	
}
