package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import algorithms.SHA256Algorithm;

/**
 * Mailbox Database Access Object class that handles mailboxes in the database
 * 
 * @author Martin Holecek
 *
 */
public class MailboxDAO{
	
	private ResultSet resultSet;
	private String domain = "derby.ac.uk";
	private Connection connection;
	
	/**
	 * Initiate Mailbox Database Access Object
	 * 
	 * @param connection the connection object which connects to the database
	 */
	public MailboxDAO(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Reset unique identifiers (UID) of all messages of the current mailbox
	 * 
	 * @param mailbox name of the mailbox
	 * @throws SQLException if the database connection failed
	 */
	public void resetAllUID(String mailbox) throws SQLException{
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_SET_INCREMENT);
		preparedStatement.executeUpdate();
		preparedStatement.close();
		preparedStatement = connection.prepareStatement(MYSQL.QUERY_RESET_UID);
		preparedStatement.setString(1, mailbox);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
	/**
	 * Check if mailbox exists in the database
	 * 
	 * @param mailbox name of the mailbox
	 * @return true if mailbox exists, false otherwise
	 * @throws SQLException if the database connection failed
	 */
	public boolean isMailboxExists(String mailbox) throws SQLException{
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_MAILBOX_EXISTS);
		preparedStatement.setString(1, mailbox);
		resultSet = preparedStatement.executeQuery();
		boolean isMailboxExists = resultSet.first();
		preparedStatement.close();
		return isMailboxExists;
	}

	/**
	 * Store token into the database
	 * 
	 * @param mailbox name of the mailbox
	 * @param token randomly generated token
	 * @throws SQLException if the database connection failed
	 */
	public void storeToken(String mailbox, String token) throws SQLException{
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_UPDATE_TOKEN);
		preparedStatement.setString(1, token);
		preparedStatement.setString(2, mailbox);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}

	/**
	 * Check if Token sent by the client is valid
	 * 
	 * @param mailbox name of the mailbox
	 * @param token the string object sent by the user
	 * @return true if the token is valid, false otherwise
	 * @throws SQLException if the database connection failed
	 */
	public boolean validateToken(String mailbox, String token) throws SQLException{
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_VALIDATE_TOKEN);
		preparedStatement.setString(1, mailbox);
		preparedStatement.setString(2, token);
		resultSet = preparedStatement.executeQuery();		
		boolean isTokenValid = resultSet.first();
		preparedStatement.close();
		return isTokenValid;
	}

	/**
	 * Check if the client has permissions to use the mailbox
	 * 
	 * @param mailbox name of the mailbox
	 * @param password the password sent by the client
	 * @return true if the client has permissions to use the mailbox, false otherwise
	 * @throws SQLException if the database connection failed
	 */
	public boolean validateMailbox(String mailbox, String password) throws SQLException{
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_VALIDATE_MAILBOX);
		preparedStatement.setString(1, mailbox);
		preparedStatement.setString(2, password);
		resultSet = preparedStatement.executeQuery();
		boolean isMailboxValid = resultSet.first();
		preparedStatement.close();
		return isMailboxValid;
	}
	
	/**
	 * Create new mailbox inside the database
	 * 
	 * @param mailbox name of the mailbox
	 * @param password of the mailbox sent by the client
	 * @param token randomly generated token assign to the mailbox
	 * @throws SQLException if the database connection failed
	 */
	public void createMailbox(String mailbox, String password, String token) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(MYSQL.QUERY_CREATE_MAILBOX);
		preparedStatement.setString(1, mailbox);
		preparedStatement.setString(2, domain);
		preparedStatement.setString(3, SHA256Algorithm.hash(password.getBytes()));
		preparedStatement.setString(4, token);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
}
