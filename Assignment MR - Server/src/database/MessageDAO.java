package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.Message;

/**
 * Message Database Access Object class that handles messages inside the database
 * 
 * @author Martin Holecek
 *
 */
public class MessageDAO{

	private ResultSet resultSet;
	private ArrayList<Message> messages;
	private ArrayList<Integer> searchUIDs;
	private boolean verbose;
	private Connection connection;

	/**
	 * Initiate Message Database Access Object
	 * 
	 * @param database the object that handles connection to the database
	 */
	public MessageDAO(Connection database, boolean verbose) {
		messages = new ArrayList<>();
		searchUIDs = new ArrayList<>();
		this.connection = database;
		this.verbose = verbose;
	}

	/**
	 * Create Message object from informations fetched from the database
	 * 
	 * @throws SQLException if the database connection failed
	 */
	private void createMessage() throws SQLException{
		Message message = new Message();
		message.setMessageID(resultSet.getInt("MessageID"));
		message.setMessageUID(resultSet.getInt("UID"));
		message.setSubject(resultSet.getString("Subject"));
		message.setSender(resultSet.getString("Sender"));
		message.setRecipients(resultSet.getString("Recipient"));
		message.setDate(resultSet.getDate("Date"));
		message.setBody(resultSet.getString("Mime"));
		message.setBody(resultSet.getString("Body"));
		displayVerboseMessage(message);
		messages.add(message);
	}

	/**
	 * Print message object to the console
	 * 
	 * @param message the message object
	 */
	private void displayVerboseMessage(Message message) {
		if (verbose) {
			System.out.println("///////////////// BEGIN MAIL /////////////////");
			System.out.println(message);
			System.out.println("////////////////// END MAIL //////////////////");
		}
	}

	/**
	 * Fetch messages from the database by the sequence range of unique identifiers (UID)
	 * 
	 * @param mailbox name of the mailbox
	 * @param firstUID the first number of the sequence
	 * @param lastUID the last number of the sequence
	 * @return the ArrayList of message objects in the range of first and last number inclusive
	 * @throws SQLException if the database connection failed
	 */
	public ArrayList<Message> getMessages(String mailbox, int firstUID, int lastUID) throws SQLException {
		messages.clear();
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_MESSAGES_UID_SEQUENCE);
		int counter = 1;
		preparedStatement.setString(counter++, mailbox);
		preparedStatement.setInt(counter++, firstUID);
		preparedStatement.setInt(counter++, lastUID);
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {  
			createMessage();
		}
		preparedStatement.close();
		return messages;
	}

	/**
	 * Fetch a single message from the database by its unique identifier (UID)
	 * 
	 * @param mailbox name of the mailbox
	 * @param firstUID the UID of the message inside the database
	 * @return the message object
	 * @throws SQLException if the database connection failed
	 */
	public ArrayList<Message> getMessages(String mailbox, int firstUID) throws SQLException {
		messages.clear();
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_SINGLE_MESSAGE);
		int counter = 1;
		preparedStatement.setString(counter++, mailbox);
		preparedStatement.setInt(counter++, firstUID);
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {  
			createMessage();
		}
		preparedStatement.close();
		return messages;
	}

	/**
	 * Fetch all messages of the current mailbox from the database
	 * 
	 * @param mailbox name of the mailbox
	 * @return the ArrayList of the message objects
	 * @throws SQLException if the database connection failed
	 */
	public ArrayList<Message> getMessages(String mailbox) throws SQLException{
		messages.clear();
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_MESSAGES);
		preparedStatement.setString(1, mailbox);
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {  
			createMessage();
		}
		preparedStatement.close();
		return messages;
	}

	/**
	 * Fetch messages from the database by the sequence range of unique identifiers (UID) and by the flags.
	 * 
	 * @param mailbox name of the mailbox
	 * @param flags the ArrayList of flags
	 * @param firstUID the first number of the sequence
	 * @param lastUID the last number of the sequence
	 * @return the ArrayList of the message objects
	 * @throws SQLException if the database connection failed
	 */
	public ArrayList<Message> getMessages(String mailbox, ArrayList<String> flags, int firstUID, int lastUID) throws SQLException {
		messages.clear();
		String query = constructQuery(flags);
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_MESSAGES_UID_SEQUENCE + query);
		int counter = 1;
		preparedStatement.setString(counter++, mailbox);
		preparedStatement.setInt(counter++, firstUID);
		preparedStatement.setInt(counter++, lastUID);
		for (String flag : flags) {
			preparedStatement.setString(counter++, flag);
		}
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {  
			createMessage();
		}
		preparedStatement.close();
		return messages;
	}

	/**
	 * Fetch messages from the database by the chosen flags.
	 * 
	 * @param mailbox name of the mailbox
	 * @param flags the ArrayList of flags
	 * @return the ArrayList of the message objects
	 * @throws SQLException if the database connection failed
	 */
	public ArrayList<Message> getMessages(String mailbox, ArrayList<String> flags) throws SQLException {
		messages.clear();
		String query = constructQuery(flags);
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_MESSAGES + query);
		int counter = 1;
		preparedStatement.setString(counter++, mailbox);
		for (String flag : flags) {
			preparedStatement.setString(counter++, flag);
		}
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {  
			createMessage();
		}
		preparedStatement.close();
		return messages;
	}

	/**
	 * Construct flag query from ArrayList of flags
	 * 
	 * @param flags the ArrayList of flags
	 * @return the string object of SQL Query
	 */
	private String constructQuery(ArrayList<String> flags) {
		String query = "";
		if (!flags.isEmpty()) {
			query = " AND (";
			for (int i = 0; i < flags.size(); i++) {
				if (i == flags.size() - 1) {
					query += "Flag = ? )";
				} else {
					query += "Flag = ? OR ";
				}
			}
		}
		return query;
	}

	/**
	 * Count number of all message inside current mailbox
	 * 
	 * @param mailbox name of the mailbox
	 * @return the integer number of the messages inside current mailbox
	 * @throws SQLException if the database connection failed
	 */
	public int getNumberMessagesAll(String mailbox) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_COUNT_ALL_MESSAGES);
		preparedStatement.setString(1, mailbox);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt(1);
	}

	/**
	 * Count number of messages inside current mailbox which corresponds to given flag
	 * 
	 * @param mailbox name of the mailbox
	 * @param flag the string object contains the flag value
	 * @return the integer number of the messages inside current mailbox
	 * @throws SQLException if the database connection failed
	 */
	public int getNumberMessages(String mailbox, String flag) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_COUNT_MESSAGES);
		preparedStatement.setString(1, mailbox);
		preparedStatement.setString(2, flag);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt(1);
	}

	/**
	 * Update flag of the message by its ID
	 * 
	 * @param mailbox name of the mailbox
	 * @param messageID the ID of the message in the database
	 * @param flag the string object contains the flag value
	 * @return true if the message flag has been changed, false otherwise
	 * @throws SQLException if the database connection failed
	 */
	public boolean updateMessageFlag(String mailbox, int messageID, String flag) throws SQLException{
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_UPDATE_FLAG);
		preparedStatement.setString(1, flag);
		preparedStatement.setInt(2, messageID);
		preparedStatement.setString(3, mailbox);
		int action = preparedStatement.executeUpdate();
		preparedStatement.close();
		if(action > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Delete all messages flagged as DELETED from the database
	 * 
	 * @param mailbox name of the mailbox
	 * @throws SQLException if the database connection failed
	 */
	public void deleteMessages(String mailbox) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_DELETE_MESSAGES);
		preparedStatement.setString(1, mailbox);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
	/**
	 * Searches the mailbox for messages that match the given searching criteria.
	 * 
	 * @param mailbox name of the mailbox
	 * @param searchKey the object corresponds to the field inside the database
	 * @param searchValue the matching string 
	 * @return the unique identifiers (UID) of the matching messages
	 * @throws SQLException if the database connection failed
	 */
	public ArrayList<Integer> searchMessages(String mailbox, String searchKey, String searchValue) throws SQLException{
		searchUIDs.clear();
		String query = " AND " + searchKey + " LIKE ? ORDER BY `UID` ASC";
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_MESSAGES + query);
		preparedStatement.setString(1, mailbox);
		preparedStatement.setString(2, "%" + searchValue + "%");
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {  
			searchUIDs.add(resultSet.getInt("UID"));
		}
		preparedStatement.close();
		return searchUIDs;
	}
	
	/**
	 * Searches the mailbox for messages that match the given searching criteria.
	 * This method searches in all fields in the database.
	 * 
	 * @param mailbox name of the mailbox
	 * @param searchValue the matching string 
	 * @return the unique identifiers (UID) of the matching messages
	 * @throws SQLException if the database connection failed
	 */
	public ArrayList<Integer> searchMessagesAll(String mailbox, String searchValue) throws SQLException{
		searchUIDs.clear();
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_SEARCH_ALL);
		preparedStatement.setString(1, mailbox);
		preparedStatement.setString(2, "%" + searchValue + "%");
		preparedStatement.setString(3, "%" + searchValue + "%");
		preparedStatement.setString(4, "%" + searchValue + "%");
		preparedStatement.setString(5, "%" + searchValue + "%");
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {  
			searchUIDs.add(resultSet.getInt("UID"));
		}
		preparedStatement.close();
		return searchUIDs;
	}
	
	/**
	 * Search for specific date where the date of the message is within or later than the specified date.
	 * 
	 * @param mailbox name of the mailbox
	 * @param searchValue the matching string 
	 * @return the unique identifiers (UID) of the matching messages
	 * @throws SQLException if the database connection failed
	 */
	public ArrayList<Integer> searchMessagesDateSince(String mailbox, Date searchValue) throws SQLException{
		searchUIDs.clear();
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_DATE_SINCE);
		preparedStatement.setString(1, mailbox);
		preparedStatement.setDate(2, searchValue);
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {  
			searchUIDs.add(resultSet.getInt("UID"));
		}
		preparedStatement.close();
		return searchUIDs;
	}
	
	/**
	 * Search for specific date where the date of the message is earlier than the specified date.
	 * 
	 * @param mailbox name of the mailbox
	 * @param searchValue the matching string 
	 * @return the unique identifiers (UID) of the matching messages
	 * @throws SQLException if the database connection failed
	 */
	public ArrayList<Integer> searchMessagesDateUntil(String mailbox, Date searchValue) throws SQLException{
		searchUIDs.clear();
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_DATE_UNTIL);
		preparedStatement.setString(1, mailbox);
		preparedStatement.setDate(2, searchValue);
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {  
			searchUIDs.add(resultSet.getInt("UID"));
		}
		preparedStatement.close();
		return searchUIDs;
	}
}
