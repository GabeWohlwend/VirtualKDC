import java.nio.charset.Charset;
import java.security.Key;

public class StoredClient {
	
	private byte[] message = "".getBytes(Charset.forName("UTF-8"));
	private String name;
	private Key publicKey;

	StoredClient(String givenName, Key givenPublicKey){
		name = givenName;
		publicKey = givenPublicKey;
	}
	StoredClient(){
		name = "Not Given";
		publicKey = null;
	}
	
	
	public String getName(){
		return name;
	}
	
	public Key getPubKey(){
		return publicKey;
	}
	
	public byte[] getMessage(){
		return message;
	}
	
	public void setMessage(byte [] message){
		this.message = message;
	}
}