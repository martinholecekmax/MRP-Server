package commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import database.DBConnect;
import server.Message;
import server.Session;

/**
 * This class handles FETCH Command which is used for retrieving messages from the server
 * 
 * @author Martin Holecek
 *
 */
public class FetchCommand {
	private Session session;
	private DBConnect database;
	private ArrayList<String> arguments;
	private ArrayList<Message> messages;
	private ArrayList<String> flags;
	private int firstUID; 
	private int lastUID; 
	private boolean isSingleMessage;
	private boolean isSequence;
	private boolean allFlag;

	private static final String ALL_FLAG = "ALL";
	private static final String RECENT_FLAG = "RECENT";
	private static final String SENT_FLAG = "SENT";
	private static final String DRAFT_FLAG = "DRAFT";
	private static final String SEEN_FLAG = "SEEN";
	private static final String DELETED_FLAG = "DELETED";

	private static final String SPACE_SYMBOL = " ";
	private static final String SPLIT_SYMBOL_COLON = ":";
	private static final String CRLF = "\r\n";
	private static final char COLON_CHAR = ':';
	private static final int SEQUENCE_WITHOUT_COLON = 0;
	private static final int NUMBER_COLONS_IN_SEQUENCE = 1;
	private static final int SECOND_ELEMENT = 1;
	private static final int FIRST_ELEMENT = 0;
	private static final int SEQUENCE_LENGTH = 2;
	private static final int ARRAY_SECOND_ELEMENT = 1;

	/**
	 * Initiate Fetch Command
	 * 
	 * @param session the object that handles connection between server and client
	 * @param database the object that handles connection to the database
	 */
	public FetchCommand(Session session, DBConnect database) {
		this.session = session;
		this.database = database;
		arguments = new ArrayList<>();
		messages = new ArrayList<>();
		flags = new ArrayList<>();
	}

	/**
	 * Execute FETCH Command
	 * 
	 * @param mailbox name of the mailbox
	 * @param input message sent by the client
	 * @return false if there was any error during executing of this command
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	public boolean execute(String mailbox ,String input) throws IOException, SQLException {
		initiateFetch();
		parseArguments(input);

		if (arguments.isEmpty()) {
			session.write("BAD FETCH syntax error");
			return false;
		}

		if (isSequence(arguments.get(FIRST_ELEMENT))) {
			arguments.remove(FIRST_ELEMENT);
		}

		if (!areFlagsValidSyntax(mailbox, arguments)) {
			session.write("BAD FETCH syntax error");
			return false;
		}

		retrieveMessagesFromDB(mailbox);
		consolidateMessages();

		if (messages.isEmpty()) {
			session.write("BAD No messages found!");			
		} else {
			sendMessages();
			session.write("OK FETCH Completed");
		}
		return true;
	}

	/**
	 * Clear messages and flags ArrayList and reset internal flags
	 */
	private void initiateFetch() {
		messages.clear();
		flags.clear();
		isSingleMessage = false;
		isSequence = false;
		allFlag = false;
	}

	/**
	 * Retrieve Messages from the database
	 * 
	 * @param mailbox name of the mailbox
	 * @throws SQLException if the database connection failed
	 */
	private void retrieveMessagesFromDB(String mailbox) throws SQLException {
		if (isSingleMessage) {
			messages.addAll(database.getMessageDAO().getMessages(mailbox, firstUID));
		} else if (!allFlag && isSequence) {
			messages.addAll(database.getMessageDAO().getMessages(mailbox, flags, firstUID, lastUID));
		} else if (isSequence) {
			messages.addAll(database.getMessageDAO().getMessages(mailbox, firstUID, lastUID));
		} else if (!allFlag) {
			messages.addAll(database.getMessageDAO().getMessages(mailbox, flags));
		}  else if (allFlag) {
			messages.addAll(database.getMessageDAO().getMessages(mailbox));
		}
	}

	/**
	 * Check if the flags sent by the client valid
	 * 
	 * @param mailbox name of the mailbox
	 * @param arguments list of arguments sent by the client
	 * @return true if the flags are valid, false if any of the flag is not a valid syntax
	 * @throws SQLException if the database connection failed
	 */
	private boolean areFlagsValidSyntax(String mailbox, ArrayList<String> arguments) throws SQLException {		
		for (String string : arguments) {
			if (string.equalsIgnoreCase(ALL_FLAG)) {
				allFlag = true;
			} else if (string.equalsIgnoreCase(RECENT_FLAG)) {
				flags.add(RECENT_FLAG);
			} else if (string.equalsIgnoreCase(SENT_FLAG)) {
				flags.add(SENT_FLAG);
			} else if (string.equalsIgnoreCase(DRAFT_FLAG)) {
				flags.add(DRAFT_FLAG);
			} else if (string.equalsIgnoreCase(SEEN_FLAG)) {
				flags.add(SEEN_FLAG);
			} else if (string.equalsIgnoreCase(DELETED_FLAG)) {
				flags.add(DELETED_FLAG);
			}  else {
				flags.clear();
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if string object is valid sequence
	 * 
	 * @param input the string object to be tested
	 * @return true if the string is sequence, false otherwise
	 */
	private boolean isSequence(String input) {
		try {
			if(countColonsInString(input) == NUMBER_COLONS_IN_SEQUENCE) {
				return splitSequenceByColon(input);
			} else if (countColonsInString(input) == SEQUENCE_WITHOUT_COLON) {
				firstUID = parseSequence(input);
				isSingleMessage = true;
				return true;
			}
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Check if sequence contains only one colon
	 * 
	 * @param input the string object to be tested
	 * @return true if there is only single colon in the sequence, false otherwise
	 * @throws NumberFormatException if sequence is not a number
	 */
	private boolean splitSequenceByColon(String input) throws NumberFormatException{
		String splitSequence[] = input.split(SPLIT_SYMBOL_COLON);
		if (splitSequence.length == SEQUENCE_LENGTH) {
			firstUID = parseSequence(splitSequence[FIRST_ELEMENT]);
			lastUID = parseSequence(splitSequence[SECOND_ELEMENT]);			
			isSequence = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return sequence number parsed from the string object
	 * 
	 * @param sequence the string object to be parsed
	 * @return sequence number
	 * @throws NumberFormatException if the sequence is not a number
	 */
	private int parseSequence(String sequence) throws NumberFormatException{
		return Integer.parseInt(sequence);
	}

	/**
	 * Return number of colons in the string
	 * 
	 * @param input the string object to be tested
	 * @return number of colons
	 */
	private int countColonsInString(String input) {
		int counter = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == COLON_CHAR) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Remove duplicates of the messages fetched from the database
	 */
	private void consolidateMessages() {
		ArrayList <Message> uniqueArrayList = new ArrayList<Message>();
		for (int i = 0; i < messages.size(); i++){
			if (!uniqueArrayList.contains(messages.get(i))){
				uniqueArrayList.add(messages.get(i));
			}
		}
		messages.clear();
		messages.addAll(uniqueArrayList);
	}

	/**
	 * Send messages to the client
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private void sendMessages() throws IOException {
		for (Message message : messages) {
			session.write("* FETCH ID " + message.getMessageID() + SPACE_SYMBOL + "SIZE " + message.toString().length() + CRLF);
			session.write(message + CRLF);
		}
	}

	/**
	 * Parse the arguments of the message sent by the client
	 * 
	 * @param input message sent by the client
	 */
	private void parseArguments(String input) {
		input = input.trim();
		arguments.clear();
		if (input.contains(SPACE_SYMBOL)) {
			String clientMessageArray[] = input.split(SPACE_SYMBOL);
			for (int i = ARRAY_SECOND_ELEMENT; i < clientMessageArray.length; i++) {
				arguments.add(clientMessageArray[i]);
			}
		}
	}
}