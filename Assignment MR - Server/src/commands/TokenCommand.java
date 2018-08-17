package commands;

import java.io.IOException;
import java.sql.SQLException;
import database.DBConnect;
import database.MailboxDAO;
import server.Session;

/**
 * This class handles TOKEN Command which is used for authentication of the client
 * 
 * @author Martin Holecek
 *
 */
public class TokenCommand {
	private Session session;
	private MailboxDAO mailboxDAO;
	private String mailbox;
	private String token;	

	private static final String SPACE_SYMBOL = " ";
	private static final String EMPTY_STRING = "";
	private static final int ARRAY_SECOND_ELEMENT = 1;
	private static final int ARRAY_THIRD_ELEMENT = 2;
	private static final int THREE_ARGUMENTS = 3;

	/**
	 * Initiate TOKEN Command
	 * 
	 * @param session the object that handles connection between server and client
	 * @param database the object that handles connection to the database
	 */
	public TokenCommand(Session session, DBConnect database) {
		this.session = session;
		mailboxDAO = database.getMailboxDAO();
		mailbox = EMPTY_STRING;
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
	 * Execute TOKEN Command
	 * 
	 * @param input message sent by the client
	 * @return false if there was any error during executing of this command
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	public boolean isTokenValid(String input) throws IOException, SQLException {
		if (!parseArguments(input)) {
			session.write("BAD Authentication Failed, Parsing Arguments Error!");
			return false;
		}

		if (!mailboxDAO.validateToken(mailbox, token)){
			session.write("BAD Token Validation Failed");
			return false;
		}
		
		mailboxDAO.resetAllUID(mailbox);
		session.write("OK TOKEN Completed");
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
			token = arguments[ARRAY_THIRD_ELEMENT].trim();
			return true;
		}
		return false;		
	}
}
