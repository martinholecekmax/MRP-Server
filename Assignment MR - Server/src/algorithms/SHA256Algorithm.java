package algorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides the functionality of a message digest algorithm SHA-256
 * 
 * @author Martin Holecek
 *
 */
public class SHA256Algorithm {
	/**
	 * Completes the hash computation and converts hash of bytes to the HEX string object
	 * 
	 * @param input the array of bytes.
	 * @return the string object of the resulting hash value.
	 */
	public static String hash(byte[] input){
		String output = null;
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			sha.update(input);
			byte[] hash = sha.digest();
			output = String.format("%032X", new BigInteger(1, hash));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return output;
	}
}
