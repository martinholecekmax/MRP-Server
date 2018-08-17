package commands;

import java.io.IOException;
import server.Session;

/**
 * This class handles QUIT Command which informs the server that the client is done with the connection
 * 
 * @author Martin Holecek
 *
 */
public class QuitCommand {
	private Session session;

	private static final String SPACE_SYMBOL = " ";
	private static final int ARGUMENT_LENGTH = 1;
	
	/**
	 * Initiate QUIT Command
	 * 
	 * @param session the object that handles connection between server and client
	 */
	public QuitCommand(Session session) {
		this.session = session;
	}

	/**
	 * Execute QUIT Command
	 * 
	 * @param input message sent by the client
	 * @return true if the client has been successfully loged out;
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	public boolean execute(String input) throws IOException {
		if (parseArguments(input)) {
			session.write("OK QUIT Completed");
			return false;
		} else {
			session.write("BAD Syntax Error");
			return true;
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
