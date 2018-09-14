 package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class connects to the database
 * 
 * @author Martin Holecek
 */
public class DBConnect {

	enum ServerType{
		LOCAL_HOST
	} 

	private static final ServerType SERVER = ServerType.LOCAL_HOST;
	private final String PASSWORD = "password";
	private final String USERNAME = "user";
	private Connection connection;
	private boolean verbose;
	
	public DBConnect(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Returns connection object which connects to the MYSQL database
	 * 
	 * @return connection object
	 */
	public Connection getConnection() {
		return connection;
	}
		
	/**
	 * Returns Mailbox database access object
	 * 
	 * @return mailbox database access object
	 */
	public MailboxDAO getMailboxDAO() {
		return new MailboxDAO(connection);
	}

	/**
	 * Returns Message database access object
	 * 
	 * @return message database access object
	 */
	public MessageDAO getMessageDAO() {
		return new MessageDAO(connection, verbose);
	}

	/**
	 * Connect to the database using DriverManager class
	 * 
	 * @throws SQLException if the database connection failed
	 */
	public void connect() throws SQLException {
		if (connection != null) {
			return;
		}
		
		switch (SERVER) {		
		case LOCAL_HOST:
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/smtp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", USERNAME, PASSWORD);
			break;		
		}
	}

	/**
	 * Disconnect from current database
	 * 
	 * @throws SQLException if the database connection failed
	 */
	public void disconnect() throws SQLException{
		connection.close();
	}
}
