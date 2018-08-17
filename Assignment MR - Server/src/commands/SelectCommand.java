package commands;

import java.io.IOException;
import java.sql.SQLException;
import database.DBConnect;
import database.MailboxDAO;
import database.MessageDAO;
import server.Session;

/**
 * This class handles SELECT Command which selects a mailbox so that messages in the mailbox can be accessed
 * 
 * @author Martin Holecek
 *
 */
public class SelectCommand {	
	private Session session;
	private MailboxDAO mailboxDAO;
	private MessageDAO messageDAO;
	
	private static final String SPACE_SYMBOL = " ";
	private static final int NO_ARGUMENTS = 1;
	private static final String[] FLAGS = 
		{"RECENT", "SENT", "DRAFT", "SEEN", "DELETED"};
	
	/**
	 * Initiate SELECT Command
	 * 
	 * @param session the object that handles connection between server and client
	 * @param database the object that handles connection to the database
	 */
	public SelectCommand(Session session, DBConnect database) {
		this.session = session;
		mailboxDAO = database.getMailboxDAO();
		messageDAO = database.getMessageDAO();
	}

	/**
	 * Execute SELECT Command
	 * 
	 * @param mailbox name of the mailbox
	 * @param input message sent by the client
	 * @return return false if there was any error during executing of this command
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	public boolean execute(String mailbox, String input) throws SQLException, IOException {
		if (!parseArguments(input)) {
			session.write("BAD SELECT command does not accept arguments!");
			return false;
		}		
		
		mailboxDAO.resetAllUID(mailbox);		
		final int numberMessages = messageDAO.getNumberMessagesAll(mailbox);
		session.write("* "  + numberMessages + " EXISTS");
		sendEachFlag(mailbox);
		session.write("OK SELECT Completed");
		return true;
	}

	/**
	 * Send number of messages of individual flags
	 * 
	 * @param mailbox name of the mailbox
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	private void sendEachFlag(String mailbox) throws SQLException, IOException {
		for (int index = 0; index < FLAGS.length; index++) {
			int number = messageDAO.getNumberMessages(mailbox, FLAGS[index]);
			session.write("* "  + number + SPACE_SYMBOL + FLAGS[index]);			
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
		if (arguments.length != NO_ARGUMENTS) {
			return false;
		}
		return true;
	}
}
