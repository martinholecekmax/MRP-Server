package commands;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import server.Session;
import server.Session.Encryption;

/**
 * This class handles AUTH Command which is used for setting encryption mode
 * 
 * @author Martin Holecek
 *
 */
public class AuthenticationCommand {
	private String algorithmName;
	private Session session;
	
	private static final String AES_ECB = "AES/ECB";
	private static final String AES_CBC = "AES/CBC";
	private static final String DES_ECB = "DES/ECB";
	private static final String DES_CBC = "DES/CBC";
	private static final String PLAIN = "PLAIN";
	
	private static final String SPACE_SYMBOL = " ";
	private static final String EMPTY_STRING = "";
	private static final int ARRAY_SECOND_ELEMENT = 1;
	private static final int TWO_ARGUMENTS = 2;
	
	/**
	 * Initiate Authentication Command
	 * 
	 * @param session the object that handles connection between server and client
	 */
	public AuthenticationCommand(Session session) {
		this.session = session;
		algorithmName = EMPTY_STRING;
	}
	
	/**
	 * Execute AUTH Command which will initiate encryption
	 * 
	 * @param input message sent by the client
	 * @return false if there is wrong argument, true otherwise
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws InvalidKeyException if the given key is inappropriate for initializing this cipher, or requires algorithm parameters that cannot be determined from the given key, or if the given key has a keysize that exceeds the maximum allowable keysize (as determined from the configured jurisdiction policy files). 
	 * @throws NoSuchAlgorithmException if transformation is null, empty, in an invalid format, or if a CipherSpi implementation for the specified algorithm is not available from the specified Provider object. 
	 * @throws InvalidKeySpecException if the given key specification is inappropriate for this secret-key factory to produce a secret key.
	 */
	public boolean execute(String input) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		if (!parseArguments(input)) {
			session.write("BAD Authentication Failed, Parsing Arguments Error!");
			return false;
		}
		
		switch (algorithmName) {
		case AES_ECB:
			session.selectEncryptionMode(Encryption.AES_ECB);
			session.write("* AES/ECB Encryption is established");
			session.write("OK AUTH Completed");	
			break;
		case AES_CBC:
			session.selectEncryptionMode(Encryption.AES_CBC);
			session.write("* AES/CBC Encryption is established");
			session.write("OK AUTH Completed");	
			break;
		case DES_ECB:			
			session.selectEncryptionMode(Encryption.DES_ECB);
			session.write("* DESede/ECB Encryption is established");
			session.write("OK AUTH Completed");	
			break;
		case DES_CBC:
			session.selectEncryptionMode(Encryption.DES_CBC);
			session.write("* DESede/CBC Encryption is established");			
			session.write("OK AUTH Completed");	
			break;
		case PLAIN:
			session.selectEncryptionMode(Encryption.PLAIN);
			session.write("* WARNING, SENDING MESSAGES WITHOUT ENCRYPTION IS NOT SECURE!");	
			session.write("OK AUTH Completed");	
			break;
		default:
			session.write("BAD Syntax Error, Algorithm name is not valid!");			
			return false;
		}
		return true;
	}
	
	/**
	 * Parse the arguments of the message sent from the client
	 * 
	 * @param input message sent by the client
	 * @return true if parsing has been successful, false otherwise
	 */
	private boolean parseArguments(String input) {
		String arguments[] = input.split(SPACE_SYMBOL);
		if (arguments.length == TWO_ARGUMENTS) {
			algorithmName = arguments[ARRAY_SECOND_ELEMENT].trim().toUpperCase();
			return true;
		}
		return false;		
	}
}
