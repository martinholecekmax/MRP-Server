package commands;

import java.io.IOException;
import server.Session;

/**
 * This class handles LOGOUT Command which informs the server that the client is done with the mailbox
 * 
 * @author Martin Holecek
 *
 */
public class LogoutCommand {
	private Session session;
	
	private static final String SPACE_SYMBOL = " ";
	private static final int ARGUMENT_LENGTH = 1;
	
	/**
	 * Initiate Logout Command
	 * 
	 * @param session the object that handles connection between server and client
	 */
	public LogoutCommand(Session session) {
		this.session = session;
	}
	
	/**
	 * Logout the client
	 * 
	 * @param input message sent by the client
	 * @return true if the client has been successfully loged out;
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	public boolean execute(String input) throws IOException {
		if (parseArguments(input)) {
			session.write("OK LOGOUT Completed");
			return true;
		} else {
			session.write("BAD Syntax Error");
			return false;
		}
	}
	
	/**
	 * Parse the arguments of the message sent from the client
	 * 
	 * @param input message sent by the client
	 * @return true if parsing has been successful, false otherwise
	 */
	private boolean parseArguments(String input) {
		String arguments[] = input.split(SPACE_SYMBOL);
		if (arguments.length > ARGUMENT_LENGTH) {
			return false;
		}
		return true;		
	}
}
