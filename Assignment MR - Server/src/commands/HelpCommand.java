package commands;

import java.io.IOException;

import server.Session;

/**
 * This class handles HELP Command sends detailed information to the client
 * 
 * @author Martin Holecek
 *
 */
public class HelpCommand {	
	private Session session;

	private int numerOfArguments;	
	private String command;

	private static final String AUTH = "AUTH";
	private static final String LOGIN = "LOGIN";
	private static final String TOKEN = "TOKEN";
	private static final String CREATE = "CREATE";
	private static final String SELECT = "SELECT";
	private static final String FETCH = "FETCH";
	private static final String SEARCH = "SEARCH";
	private static final String CHANGE = "CHANGE";
	private static final String EXPUNGE = "EXPUNGE";
	private static final String LOGOUT = "LOGOUT";
	private static final String NOOP = "NOOP";
	private static final String QUIT = "QUIT";

	private static final String EMPTY_STRING = "";
	private static final String SPACE_SYMBOL = " ";
	private static final int ONE_ARGUMENT = 2;
	private static final int ARRAY_SECOND_ELEMENT = 1;
	private static final int NO_ARGUMENT = 1;

	/**
	 * Initiate Help Command
	 * 
	 * @param session the object that handles connection between server and client
	 */
	public HelpCommand(Session session) {
		this.session = session;
		command = EMPTY_STRING;
		numerOfArguments = NO_ARGUMENT;
	}

	/**
	 * Execute HELP Command
	 * 
	 * @param input message sent by the client
	 * @return false if there was any error during executing of this command
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	public boolean execute(String input) throws IOException {
		if (!parseArguments(input)) {
			session.write("BAD Authentication Failed, Parsing Arguments Error!");
			return false;
		}

		if(numerOfArguments == NO_ARGUMENT) {
			sendHelpResponse();
		} else {
			sendSpecificResponse();
		}

		return true;
	}

	/**
	 * Send generic help response to the client
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private void sendHelpResponse() throws IOException {
		session.write("* MOST COMMANDS ARE ONLY VALID IN CERTAIN STATE!");
		session.write("* AUTHENTICATED STATE COMMANDS: AUTH, LOGIN, TOKEN, CREATE");
		session.write("* SELECT STAGE COMMANDS: SELECT");
		session.write("* CONTROL STAGE COMMANDS : FETCH, EXPUNGE, CHANGE, SEARCH, LOGOUT");
		session.write("* COMMANDS PERMITTED IN ANY STATE: NOOP, HELP, QUIT");
		session.write("* For more info use HELP<SP><COMMAND>");
		session.write("OK HELP Completed");
	}

	/**
	 * Send specific response about functionality of the specified command to the client
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private void sendSpecificResponse() throws IOException {
		switch (command) {
		case AUTH:
			session.write("* Syntax: AUTH<SP><ARGUMENT>");
			session.write("* ARGUMENTS:");
			session.write("* AES/CBC - AES Encryption with Cipher Block Chaining mode");
			session.write("* AES/ECB - AES Encryption with Electronic CodeBook mode");
			session.write("* DES/CBC - DESede Encryption with Cipher Block Chaining mode");
			session.write("* DES/ECB - DESede Encryption with Electronic CodeBook mode");
			session.write("OK HELP Completed");
			break;
		case LOGIN:			
			session.write("* Syntax: LOGIN<SP><MAILBOX><SP><PASSWORD>");
			session.write("OK HELP Completed");
			break;
		case TOKEN:			
			session.write("* Syntax: TOKEN<SP><MAILBOX><SP><TOKEN>");
			session.write("OK HELP Completed");
			break;
		case CREATE:			
			session.write("* Syntax: CREATE<SP><MAILBOX><SP><PASSWORD>");
			session.write("* CREATE Command will create new Mailbox");
			session.write("OK HELP Completed");
			break;
		case SELECT:			
			session.write("* Syntax: SELECT");
			session.write("* SELECT Command does not accept arguments");
			session.write("* Function: Command sets all messages UID number sorted by date which message has been recieved");
			session.write("* WARNING: SELECT Command must be called after login and before using Mailbox CONTROL Commands");
			session.write("* CONTROL COMMANDS: FETCH, SEARCH, CHANGE, EXPUNGE AND LOGOUT");		
			session.write("OK HELP Completed");
			break;
		case FETCH:			
			session.write("* Syntax: FETCH<SP><SEQUENCE><SP><FLAG> | FETCH<SP><FLAG>");
			session.write("* SEQUENCE: <NUMBER><:><NUMBER> | <NUMBER>");
			session.write("* FLAG: ALL, RECENT, SENT, DRAFT, SEEN or DELETED");
			session.write("* FETCH Command supports argument chaining -> FETCH<SP><FLAG><SP><FLAG>");
			session.write("OK HELP Completed");
			break;
		case SEARCH:			
			session.write("* Syntax: SEARCH<SP><SEARCH_KEY><=><[SEARCH_VALUE]>");
			session.write("* SEARCH_KEY: ALL, BODY, SUBJECT, SENDER, RECIPIENT, SINCE or UNTIL");
			session.write("* SINCE and UNTIL: SEARCH_VALUE must be Date formated [yyyy-MM-dd]");		
			session.write("OK HELP Completed");
			break;
		case CHANGE:			
			session.write("* Syntax: CHANGE<SP><MESSAGE_ID><SP><FLAG>");
			session.write("* WARNING: MESSAGE_ID is ID not UID of the message");
			session.write("* FLAG: RECENT, SENT, DRAFT, SEEN or DELETED");		
			session.write("OK HELP Completed");
			break;
		case EXPUNGE:			
			session.write("* Syntax: EXPANGE");
			session.write("* EXPANGE Command does not accept arguments");
			session.write("* Function: EXPUNGE Command will delete all messages marked as DELETED from database");	
			session.write("OK HELP Completed");
			break;
		case LOGOUT:			
			session.write("* Syntax: LOGOUT");
			session.write("* LOGOUT Command does not accept arguments");
			session.write("OK HELP Completed");
			break;
		case NOOP:			
			session.write("* Syntax: NOOP");
			session.write("* NOOP Command does not accept arguments");
			session.write("* Function: This command will keep connection with server \"alive\"");	
			session.write("OK HELP Completed");
			break;
		case QUIT:			
			session.write("* Syntax: QUIT");
			session.write("* QUIT Command does not accept arguments");
			session.write("* Function: Terminate connection and close the transmission channel");
			session.write("OK QUIT Completed");
			break;
		default:
			session.write("BAD Argument is not valid Command!");
			break;
		}
	}

	/**
	 * Parse the arguments of the message sent by the client
	 * 
	 * @param input message sent by the client
	 * @return true if parsing has been successful, false otherwise
	 */
	private boolean parseArguments(String input) {
		String arguments[] = input.split(SPACE_SYMBOL);
		if (arguments.length == ONE_ARGUMENT) {
			command = arguments[ARRAY_SECOND_ELEMENT].trim().toUpperCase();
			numerOfArguments = ONE_ARGUMENT;
			return true;
		} else if (arguments.length == NO_ARGUMENT) {
			numerOfArguments = NO_ARGUMENT;
			return true;
		}
		return false;		
	}
}
