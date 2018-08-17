package commands;

import java.io.IOException;
import server.Session;

/**
 * This class handles NOOP Command which can be used to reset any inactivity autologout timer on the server
 * 
 * @author Martin Holecek
 *
 */
public class NoopCommand {
	private Session session;

	private static final String SPACE_SYMBOL = " ";
	private static final int ARGUMENT_LENGTH = 1;
	
	/**
	 * Initiate NOOP Command
	 * 
	 * @param session the object that handles connection between server and client
	 */
	public NoopCommand(Session session) {
		this.session = session;
	}

	/**
	 * Execute NOOP Command
	 * 
	 * @param input message sent by the client
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	public void execute(String input) throws IOException {
		if (parseArguments(input)) {
			session.write("OK NOOP Completed");
		} else {
			session.write("BAD Syntax Error");
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
