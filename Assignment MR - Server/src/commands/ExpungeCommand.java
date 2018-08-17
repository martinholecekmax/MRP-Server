package commands;

import java.io.IOException;
import java.sql.SQLException;
import database.DBConnect;
import database.MailboxDAO;
import database.MessageDAO;
import server.Session;

/**
 * This class handles EXPUNGE Command which is used for removing messages flaged as DELETED
 * 
 * @author Martin Holecek
 *
 */
public class ExpungeCommand {
	private Session session;
	private MailboxDAO mailboxDAO;
	private MessageDAO messageDAO;
	
	private static final int NO_ARGUMENTS = 1;
	private static final String SPLIT_SYMBOL_SPACE = " ";
	
	/**
	 * Initiate Expunge Command
	 * 
	 * @param session the object that handles connection between server and client
	 * @param database the object that handles connection to the database
	 */
	public ExpungeCommand(Session session, DBConnect database) {
		this.session = session;
		mailboxDAO = database.getMailboxDAO();
		messageDAO = database.getMessageDAO();
	}
	
	/**
	 * Execute EXPUNGE Command
	 * 
	 * @param mailbox name of the mailbox
	 * @param input message sent by the client
	 * @return false if there was any error during executing of this command
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	public boolean execute(String mailbox, String input) throws IOException, SQLException {
		if (!parseArguments(input)) {
			session.write("BAD EXPUNGE command does not accept arguments!");
			return false;
		}	
		
		messageDAO.deleteMessages(mailbox);
		mailboxDAO.resetAllUID(mailbox);		
		session.write("OK EXPUNGE Completed");
		return true;
	}
	
	/**
	 * Parse the arguments of the message sent by the client
	 * 
	 * @param input message sent by the client
	 * @return true if parsing has been successful, false otherwise
	 */
	private boolean parseArguments(String input) {
		String arguments[] = input.split(SPLIT_SYMBOL_SPACE);
		if (arguments.length != NO_ARGUMENTS) {
			return false;
		}
		return true;
	}
}
