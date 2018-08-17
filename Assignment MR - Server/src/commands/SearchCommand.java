package commands;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import database.DBConnect;
import database.MessageDAO;
import server.Session;

/**
 * This class handles SEARCH Command which searches the mailbox for messages that match the given searching criteria
 * 
 * @author Martin Holecek
 *
 */
public class SearchCommand {
	private Session session;
	private MessageDAO messageDAO;
	private ArrayList<Integer> messagesUID;
	private String searchKey;
	private String searchValue;
	private Date date;

	private static final String ALL = "ALL";
	private static final String BODY = "BODY";
	private static final String SUBJECT = "SUBJECT";
	private static final String SENDER = "SENDER";
	private static final String RECIPIENT = "RECIPIENT";
	private static final String SINCE = "SINCE";
	private static final String UNTIL = "UNTIL";

	private static final String SEARCH_KEY_RECIPIENT = "Recipient";
	private static final String SEARCH_KEY_SENDER = "Sender";
	private static final String SEARCH_KEY_SUBJECT = "Subject";
	private static final String SEARCH_KEY_BODY = "Body";
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	private static final String RIGHT_BRACKET = "]";
	private static final String LEFT_BRACKET = "[";
	private static final String SPACE_SYMBOL = " ";
	private static final String EQUAL_SYMBOL = "=";
	private static final String EMPTY_STRING = "";
	private static final int SUBSTRING_COMMAND = 6;
	private static final int TWO_ARGUMENTS = 2;
	private static final int ARRAY_FIRST_ELEMENT = 0;
	private static final int ARRAY_SECOND_ELEMENT = 1;

	/**
	 * Initiate SEARCH Command
	 * 
	 * @param session the object that handles connection between server and client
	 * @param database the object that handles connection to the database
	 */
	public SearchCommand(Session session, DBConnect database) {
		this.session = session;
		messageDAO = database.getMessageDAO();
		searchKey = EMPTY_STRING;
		searchValue = EMPTY_STRING;
		messagesUID = new ArrayList<>();
	}

	/**
	 * Execute SEARCH Command
	 * 
	 * @param mailbox name of the mailbox
	 * @param input message sent by the client
	 * @return return false if there was any error during executing of this command
	 * @throws IOException if the stream has been closed or another I/O error
	 * @throws SQLException if the database connection failed
	 */
	public boolean execute(String mailbox ,String input) throws IOException, SQLException {
		messagesUID.clear();

		if (!parseArguments(input)) {
			session.write("BAD Parsing Arguments Error!");
			return false;
		}

		if(!isSearchValueValid()) {
			session.write("BAD Search value must start with \"" + LEFT_BRACKET + "\" and end with \"" + RIGHT_BRACKET + "\"");
			return false;
		}

		if(!isSearchKeyValid()){			
			return false;
		}

		retrieveMessagesUIDFromDB(mailbox);	
		sendSearchResult();
		return true;
	}

	/**
	 * Retrieve messages unique identifiers (UID) from database
	 * @param mailbox name of the mailbox
	 * @throws SQLException if the database connection failed
	 */
	private void retrieveMessagesUIDFromDB(String mailbox) throws SQLException {
		if (searchKey.equals(ALL)) {
			messagesUID.addAll(messageDAO.searchMessagesAll(mailbox, searchValue));
		} else if(searchKey.equals(SINCE)) {			
			messagesUID.addAll(messageDAO.searchMessagesDateSince(mailbox, date));
		} else if(searchKey.equals(UNTIL)) {			
			messagesUID.addAll(messageDAO.searchMessagesDateUntil(mailbox, date));
		} else {			
			messagesUID.addAll(messageDAO.searchMessages(mailbox, searchKey, searchValue));
		}
	}

	/**
	 * Send search result to the client
	 * 
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private void sendSearchResult() throws IOException {
		if (messagesUID.isEmpty()) {
			session.write("* SEARCH FOUND NO RESULTS");
			session.write("OK SEARCH Completed");
		} else {
			String message = "* SEARCH";
			for (Integer integer : messagesUID) {
				message += SPACE_SYMBOL + integer;		
			}
			session.write(message);
			session.write("OK SEARCH Completed");
		}
	}

	/**
	 * Check if search VALUE is in valid format.
	 * The format MUST be [search value]
	 * 
	 * @return true if search value is valid, false otherwise
	 */
	private boolean isSearchValueValid() {
		if (searchValue.startsWith(LEFT_BRACKET) && searchValue.endsWith(RIGHT_BRACKET)) {
			searchValue = searchValue.substring(1, searchValue.length() - 1);
			return true;
		}
		return false;
	}

	/**
	 * Check if search KEY is valid
	 * 
	 * @return true if search key is valid, false otherwise
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private boolean isSearchKeyValid() throws IOException {
		switch (searchKey) {
		case ALL:
			searchKey = ALL;
			return true;
		case BODY:			
			searchKey = SEARCH_KEY_BODY;
			return true;
		case SUBJECT:
			searchKey = SEARCH_KEY_SUBJECT;
			return true;
		case SENDER:
			searchKey = SEARCH_KEY_SENDER;
			return true;
		case RECIPIENT:
			searchKey = SEARCH_KEY_RECIPIENT;
			return true;
		case SINCE:
			return parseDate();
		case UNTIL:
			return parseDate();
		default:
			session.write("BAD Search key is not valid!");
			return false;
		}
	}

	/**
	 * Parse the date sent by the client as a search VALUE
	 * 
	 * @return true if the date is valid, false otherwise
	 * @throws IOException if the stream has been closed or another I/O error
	 */
	private boolean parseDate() throws IOException {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
		try {
			java.util.Date tempDate = dateFormatter.parse(searchValue);
			date = new Date(tempDate.getTime());
			return true;
		} catch (ParseException e) {			
			session.write("BAD Parsing Date failed");
			return false;
		}
	}

	/**
	 * Parse the arguments of the message sent from the client
	 * 
	 * @param input message sent by the client
	 * @return true if parsing has been successful, false otherwise
	 */
	private boolean parseArguments(String input) {
		String message = input.substring(SUBSTRING_COMMAND);
		String arguments[] = message.split(EQUAL_SYMBOL);
		if (arguments.length == TWO_ARGUMENTS) {
			searchKey = arguments[ARRAY_FIRST_ELEMENT].trim().toUpperCase();
			searchValue = arguments[ARRAY_SECOND_ELEMENT].trim();
			return true;
		}
		return false;		
	}
}
