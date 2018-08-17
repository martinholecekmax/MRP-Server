package commands;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import database.DBConnect;
import server.Session;

/**
 * This class handles Commands sent from the client
 * 
 * @author Martin Holecek
 *
 */
public class CommandHandler {

	private enum Stage {
		AUTHENTICATE, SELECT, CONTROL
	}

	private Session session;
	private Stage stage;
	private boolean running;
	private boolean verbose;
	private String clientMessage;
	private String command;
	private String mailbox;
	
	private AuthenticationCommand authenticate;
	private LoginCommand login;
	private TokenCommand token;
	private LogoutCommand logout;
	private QuitCommand quit;
	private NoopCommand noop;
	private CreateCommand create;
	private SelectCommand select;
	private FetchCommand fetch;
	private ExpungeCommand expunge;
	private ChangeCommand change;
	private SearchCommand search;
	private HelpCommand help;
	
	private static final int ARRAY_FIRST_ELEMENT = 0;
	private static final String SPLIT_SYMBOL_SPACE = " ";
	private static final String EMPTY_STRING = "";

	/**
	 * Creates CommandHandler object which handles all available commands
	 * 
	 * @param session the object that handles connection between server and client
	 * @param database the object that handles connection to the database
	 */
	public CommandHandler(Session session, DBConnect database, boolean verbose) {
		this.session = session;
		token = new TokenCommand(session, database);
		login = new LoginCommand(session, database);
		logout = new LogoutCommand(session);
		quit = new QuitCommand(session);
		noop = new NoopCommand(session);
		create = new CreateCommand(session, database);
		select = new SelectCommand(session, database);
		fetch = new FetchCommand(session, database);
		expunge = new ExpungeCommand(session, database);
		change = new ChangeCommand(session, database);
		authenticate = new AuthenticationCommand(session);
		search = new SearchCommand(session, database);
		help = new HelpCommand(session);
		stage = Stage.AUTHENTICATE;
		mailbox = EMPTY_STRING;
		running = true;
		this.verbose = verbose;
	}
	
	/**
	 * Handles reading of commands sent by the client and check if those commands are valid
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 * @throws InvalidKeySpecException if the given key specification is inappropriate for this secret-key factory to produce a secret key.
	 * @throws NoSuchAlgorithmException if transformation is null, empty, in an invalid format, or if a CipherSpi implementation for the specified algorithm is not available from the specified Provider object. 
	 * @throws InvalidKeyException if the given key is inappropriate for initializing this cipher, or requires algorithm parameters that cannot be determined from the given key, or if the given key has a keysize that exceeds the maximum allowable keysize (as determined from the configured jurisdiction policy files). 
	 */
	public void executeClientCommand() throws IOException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		while (running) {
			clientMessage = session.read();
			displayVerboseMessage();
			if(!isMessageLengthValid()) continue;		
			if(!parseCommand(clientMessage)) continue;
			switch (stage) {			
			case AUTHENTICATE:
				authenticate();				
				break;
			case SELECT:
				selectMailbox();
				break;
			case CONTROL:
				processCommand();				
				break;
			}
		}
	}
	
	/**
	 * Process Commands which are permitted in any STATE
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private void standardCommands() throws IOException {
		switch (command) {
		case "NOOP":
			noop.execute(clientMessage);
			break;
		case "QUIT":
			running = quit.execute(clientMessage);
			break;
		case "HELP":
			help.execute(clientMessage);
			break;		
		default:
			sendSyntaxError();
			break;
		}
	}
	
	/**
	 * Process AUTHENTICATED STATE Commands
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 * @throws InvalidKeySpecException if the given key specification is inappropriate for this secret-key factory to produce a secret key.
	 * @throws NoSuchAlgorithmException if transformation is null, empty, in an invalid format, or if a CipherSpi implementation for the specified algorithm is not available from the specified Provider object. 
	 * @throws InvalidKeyException if the given key is inappropriate for initializing this cipher, or requires algorithm parameters that cannot be determined from the given key, or if the given key has a keysize that exceeds the maximum allowable keysize (as determined from the configured jurisdiction policy files). 
	 */
	private void authenticate() throws IOException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		switch (command) {
		case "AUTH":
			authenticate.execute(clientMessage);
			break;		
		case "LOGIN":
			if(login.authenticateMailbox(clientMessage)) {
				stage = Stage.SELECT;				
				mailbox = login.getMailbox();
			}
			break;
		case "CREATE":
			if(create.execute(clientMessage)) {
				stage = Stage.SELECT;				
				mailbox = create.getMailbox();
			}
			break;
		case "TOKEN":
			if(token.isTokenValid(clientMessage)) {
				stage = Stage.SELECT;				
				mailbox = token.getMailbox();
			}
			break;		
		default:
			standardCommands();
			break;
		}
	}

	/**
	 * Process SELECT STATE Command
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	private void selectMailbox() throws SQLException, IOException {
		switch (command) {
		case "SELECT":
			if(select.execute(mailbox, clientMessage)) {
				stage = Stage.CONTROL;
			}
			break;
		default:
			standardCommands();
			break;
		}
	}

	/**
	 * Process CONTROL STATE Commands
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	private void processCommand() throws IOException, SQLException {
		switch (command) {		
		case "FETCH":
			fetch.execute(mailbox, clientMessage);
			break;
		case "SEARCH":
			search.execute(mailbox, clientMessage);
			break;
		case "CHANGE":
			change.execute(mailbox, clientMessage);
			break;
		case "EXPUNGE":
			expunge.execute(mailbox, clientMessage);
			break;
		case "LOGOUT":
			if(logout.execute(clientMessage)) {
				stage = Stage.AUTHENTICATE;
			}
			break;		
		default:
			standardCommands();
			break;
		}
	}

	/**
	 * Parse the Command of the message sent by the client
	 * 
	 * @param input message sent by the client
	 * @return true if parsing has been successful, false otherwise
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private boolean parseCommand(String input) throws IOException {
		input = input.trim();
		if (input.contains(SPLIT_SYMBOL_SPACE)) {
			String[] clientMessageArray = input.split(SPLIT_SYMBOL_SPACE);
			if (clientMessageArray[ARRAY_FIRST_ELEMENT].isEmpty()) {
				session.write("BAD Invalid command!");
				return false;
			}
			command = clientMessageArray[ARRAY_FIRST_ELEMENT].toUpperCase();
			return true;
		} else {
			command = input.trim().toUpperCase();
			return true;
		}
	}

	/**
	 * Check if message is less then 512 characters, 
	 * and if is more then 512 characters then send error message to the client
	 * 
	 * @return true if message is less then 512 characters, false otherwise
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private boolean isMessageLengthValid() throws IOException {
		if (clientMessage.length() > 512) {
			session.write("BAD Error Command line is too long!");
			return false;
		}
		return true;
	}

	/**
	 * Print message sent by the client to the console
	 */
	private void displayVerboseMessage() {
		if (verbose) {
			System.out.println("Client Sends --> " + clientMessage);
		}
	}

	/**
	 * Send Syntax Error message to the client
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private void sendSyntaxError() throws IOException {
		switch (stage) {		
		case AUTHENTICATE:
			session.write("BAD Syntax Error, Mailbox is in AUTHENTICATE STATE! For more info use HELP Command.");
			break;
		case SELECT:
			session.write("BAD Syntax Error, Mailbox is in SELECT STATE! For more info use HELP Command.");
			break;
		case CONTROL:
			session.write("BAD Syntax Error, Mailbox is in CONTROL STATE! For more info use HELP Command.");
			break;
		}
	}
}
