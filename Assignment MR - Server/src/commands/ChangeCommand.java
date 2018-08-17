package commands;

import java.io.IOException;
import java.sql.SQLException;
import database.DBConnect;
import database.MessageDAO;
import server.Session;

/**
 * This class handles CHANGE Command which is used for changing flag of the message
 * 
 * @author Martin Holecek
 *
 */
public class ChangeCommand {
	
	private Session session;
	private MessageDAO messageDAO;
	
	private int messageID;
	private String messageIDString;
	private String flag;
	
	private static final int ARRAY_SECOND_ELEMENT = 1;
	private static final int ARRAY_THIRD_ELEMENT = 2;
	private static final int THREE_ARGUMENTS = 3;
	private static final String SPLIT_SYMBOL_SPACE = " ";	
	private static final String[] FLAGS = { "RECENT", "SENT", "DRAFT", "SEEN", "DELETED" };
	
	/**
	 * Initiate Change Command
	 * 
	 * @param session the object that handles connection between server and client
	 * @param database the object that handles connection to the database
	 */
	public ChangeCommand(Session session, DBConnect database) {
		this.session = session;
		messageDAO = database.getMessageDAO();
	}
	
	/**
	 * Execute CHANGE Command
	 * 
	 * @param mailbox name of the mailbox
	 * @param input message sent by the client
	 * @return return false if there was any error during executing of this command
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	public boolean execute(String mailbox, String input) throws IOException, SQLException {
		
		if (!parseArguments(input)) {
			session.write("BAD Parsing Arguments Error!");
			return false;
		}		
		
		if(!parseNumber(messageIDString)) {
			session.write("BAD Message must be number!");
			return false;
		}
				
		if(!validateFlag(flag)) {
			session.write("BAD Flag is not valid!");
			return false;
		}
		
		if (!messageDAO.updateMessageFlag(mailbox, messageID, flag)) {
			session.write("BAD Message ID is not valid!");
			return false;
		}
		
		session.write("OK CHANGE Completed");
		return true;
	}

	/**
	 * Parse message ID from the argument
	 * 
	 * @param input message ID in string object
	 * @return true if parsing has been successful, false otherwise
	 */
	private boolean parseNumber(String input) {
		try {
			messageID = Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Check if the flag argument sent from the client is valid
	 * 
	 * @param argument flag sent from the client
	 * @return true if the flag is valid, false otherwise
	 */
	private boolean validateFlag(String argument) {
		for (String flag : FLAGS) {
			if (argument.equals(flag)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Parse the arguments of the message sent from the client
	 * 
	 * @param input message sent by the client
	 * @return true if parsing has been successful, false otherwise
	 */
	private boolean parseArguments(String input) {
		String arguments[] = input.split(SPLIT_SYMBOL_SPACE);
		if (arguments.length == THREE_ARGUMENTS) {
			messageIDString = arguments[ARRAY_SECOND_ELEMENT].trim();
			flag = arguments[ARRAY_THIRD_ELEMENT].trim().toUpperCase();
			return true;
		}
		return false;		
	}
}
