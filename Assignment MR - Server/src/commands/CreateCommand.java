package commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;
import database.DBConnect;
import database.MailboxDAO;
import server.Session;

/**
 * This class handles CREATE Command which is used for creating new mailbox
 * 
 * @author Martin Holecek
 *
 */
public class CreateCommand {
	
	private Session session;
	private MailboxDAO mailboxDAO;
	private String mailbox;
	private String password;
	private String token;	

	private static final int MIN_MAILBOX_LENGHT = 8;
	private static final int MAX_MAILBOX_LENGHT = 30;
	private static final int MIN_PASSWORD_LENGHT = 8;
	private static final int MAX_PASSWORD_LENGHT = 30;
	private static final int ARRAY_SECOND_ELEMENT = 1;
	private static final int ARRAY_THIRD_ELEMENT = 2;
	private static final int TWO_ARGUMENTS = 3;
	private static final String EMPTY_STRING = "";
	
	/**
	 * Initiate Create Command
	 * 
	 * @param session the object that handles connection between server and client
	 * @param database the object that handles connection to the database
	 */
	public CreateCommand(Session session, DBConnect database) {
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
	 * Execute Creating of the new mailbox
	 * 
	 * @param input message sent by the client
	 * @return false if there was any error during executing of this command
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	public boolean execute(String input) throws IOException, SQLException {
		if (!parseArguments(input)) {
			session.write("BAD Authentication Failed, Parsing Arguments Error!");
			return false;
		}
		
		if (mailbox.length() < MIN_MAILBOX_LENGHT) {
			session.write("BAD mailbox is too short, must be minimal 8 characters!");
			return false;
		}
		
		if (mailbox.length() > MAX_MAILBOX_LENGHT) {
			session.write("BAD mailbox is too long, must be max 30 characters!");
			return false;
		}
		
		if (password.length() < MIN_PASSWORD_LENGHT) {
			session.write("BAD Password is too short, must be minimal 8 characters!");
			return false;
		}
		
		if (password.length() > MAX_PASSWORD_LENGHT) {
			session.write("BAD Password is too long, must be max 30 characters!");
			return false;
		}
		
		if (!containsOnlyASCII(mailbox)) {
			session.write("BAD Mailbox name must contain only 7-Bit ASCII Characters!");
			return false;
		}
		
		if (mailboxDAO.isMailboxExists(mailbox)) {
			session.write("BAD mailbox Already Exists, Try different one");
			return false;
		}
		
		token = UUID.randomUUID().toString();		
		mailboxDAO.createMailbox(mailbox, password, token);
		session.write("* TOKEN " + token);
		session.write("OK CREATE Completed");
		return true;
	}
	
	/**
	 * Parse the arguments of the message sent from the client
	 * 
	 * @param input message sent by the client
	 * @return true if parsing has been successful, false otherwise
	 */
	private boolean parseArguments(String input) {
		String arguments[] = input.split(" ");
		if (arguments.length == TWO_ARGUMENTS) {
			mailbox = arguments[ARRAY_SECOND_ELEMENT].trim();
			password = arguments[ARRAY_THIRD_ELEMENT].trim();
			return true;
		}
		return false;		
	}
	
	/**
	 * Determines if the specified string contains only 7-Bit ASCII Characters
	 * 
	 * @param input the string to be tested.
	 * @return true if the string contains only 7-Bit ASCII Characters, false otherwise
	 */
	private boolean containsOnlyASCII(String input) {
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) > 127) {
				return false;
			}
		}
		return true;
	}
}
