package oracle.security.crypto.core;

import java.math.BigInteger;
import java.security.interfaces.RSAKey;

public interface RSA {
	public void setKey(RSAKey key);
	public BigInteger performOp(BigInteger m);
}
