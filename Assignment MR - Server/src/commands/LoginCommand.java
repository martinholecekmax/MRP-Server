package commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;
import algorithms.SHA256Algorithm;
import database.DBConnect;
import database.MailboxDAO;
import server.Session;
import server.Session.Encryption;

/**
 * This class handles LOGIN Command which is used for authentication of the client
 * 
 * @author Martin Holecek
 *
 */
public class LoginCommand {	
	private Session session;
	private MailboxDAO mailboxDAO;
	private String mailbox;
	private String password;
	private String token;	
	
	private static final int ARRAY_SECOND_ELEMENT = 1;
	private static final int ARRAY_THIRD_ELEMENT = 2;
	private static final int THREE_ARGUMENTS = 3;
	private static final String SPACE_SYMBOL = " ";
	private static final String EMPTY_STRING = "";
	
	/**
	 * Initiate Login Command
	 * 
	 * @param session the object that handles connection between server and client
	 * @param database the object that handles connection to the database
	 */
	public LoginCommand(Session session, DBConnect database) {
		this.session = session;
		mailboxDAO = database.getMailboxDAO();
		mailbox = EMPTY_STRING;
		password = EMPTY_STRING;
		token = EMPTY_STRING;
	}
	
	/**
	 * Returns the mailbox name
	 * 
	 * @return the mailbox name
	 */
	public String getMailbox() {
		return mailbox;
	}
	
	/**
	 * Execute authentication of the client
	 * 
	 * @param input message sent by the client
	 * @return false if there was any error during executing of this command
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	public boolean authenticateMailbox(String input) throws IOException, SQLException {
		if (!parseArguments(input)) {
			session.write("BAD Authentication Failed, Parsing Arguments Error!");
			return false;
		}
		
		if(!mailboxDAO.validateMailbox(mailbox, password)) {
			session.write("BAD mailbox validation failed");
			return false;
		}
		
		token = UUID.randomUUID().toString();
		mailboxDAO.storeToken(mailbox, token);
		mailboxDAO.resetAllUID(mailbox);
		
		if (session.getMode() == Encryption.PLAIN) {
			session.write("* WARNING - ACCESS WITHOUT ENCRYPTION IS NOT SECURE!");			
		}
		
		session.write("* TOKEN " + token);
		session.write("OK LOGIN Completed");
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
		if (arguments.length == THREE_ARGUMENTS) {
			mailbox = arguments[ARRAY_SECOND_ELEMENT].trim();
			password = SHA256Algorithm.hash(arguments[ARRAY_THIRD_ELEMENT].trim().getBytes());
			return true;
		}
		return false;		
	}
}
