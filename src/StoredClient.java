import java.security.Key;

public class StoredClient {

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
}