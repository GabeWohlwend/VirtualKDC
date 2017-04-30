import java.net.*;
import java.security.interfaces.RSAKey;
import java.io.*;
import java.math.BigInteger;

import oracle.security.crypto.core.RSA;
public class Chat implements RSA{
	
	private String message;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private RSAKey key;
	private BigInteger privateKey;
	private BigInteger publicKey;
	//constructors
	public Chat(){
		
	}
	public Chat(String message){
		this.message = message;
		
	}
	
	//set methods
	public void setMessage(String message){
		this.message = message;
	}
	
	public BigInteger encrypt (BigInteger m, BigInteger message){
		return message.modPow(publicKey, m);
	}
	
	BigInteger decrypt(BigInteger m, BigInteger message) {
	      return message.modPow(privateKey, m);
	   }

	
	public String toString(){
		String str = "";
		str = "message: " + message;
		return str;
		
	}
	
	
	
	
	
	
	
	
	
	//Key Method
	@Override
	public void setKey(RSAKey key) {
		this.key = key;
		
	}
	
	//Algorithm method for encryption and decryption
	@Override
	public BigInteger performOp(BigInteger m) {
		
		return null;
	}
	
}
