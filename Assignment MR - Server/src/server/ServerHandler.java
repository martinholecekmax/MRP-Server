package server;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import commands.CommandHandler;
import database.DBConnect;

/**
 * This class initialize server and handles messages from the client
 * 
 * @author Martin Holecek
 *
 */
public class ServerHandler implements Runnable {

	private Session session;
	private DBConnect database;
	private Logger logger;
	private ArrayList<Session> clientsList;
	private CommandHandler commandHandler;
	private String domain = "derby.ac.uk";
	private boolean databaseConnected;
	private boolean verbose;

	/**
	 * Constructs server handler object that handles connection to the server
	 * 
	 * @param clientList list of the clients connected to the server
	 * @param session the object that handles connection between server and client
	 * @param logger the logger object which logs any inappropriate behaviour of the client
	 * @param verbose if true error are visible in the console otherwise errors are saved only to the logger file
	 */
	public ServerHandler(ArrayList<Session> clientList, Session session, Logger logger, boolean verbose) {
		this.session = session;
		this.clientsList = clientList;
		this.verbose = verbose;
		this.logger = logger;
		databaseConnected = true;
	}

	/**
	 * Starting the thread will called this method
	 * 
	 * {@inheritDoc} implementation of the runnable interface
	 */
	public void run() {
		connectToDatabase();
		serverHandshake();
		if (databaseConnected) {
			processCommands();
		}
		closeConnection();
	}
	
	/**
	 * Initiate connection to the MYSQL database
	 */
	private void connectToDatabase() {
		try {
			database = new DBConnect(verbose);
			database.connect();
			commandHandler = new CommandHandler(session, database, verbose);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error database connection failes", e);
			databaseConnected = false;
		}
	}
	
	/**
	 * Establishing the connection between the server and client
	 */
	private void serverHandshake() {
		try {
			if (databaseConnected) {
				session.write("OK " + domain + " Server running");
			} else {
				session.write("BAD " + domain + " not available, closing connection");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error establishing connection", e);
		}
	}
	
	/**
	 * Read and execute commands sent from the client to the server
	 */
	private void processCommands() {
		try {
			displayVerboseGreeting();
			commandHandler.executeClientCommand();
		} catch (SocketTimeoutException ex) {
			try {
				logger.log(Level.SEVERE, "Error timeout exceeded", ex);
				session.write("BAD " + domain + " SMTP MTA closing connection time out exceeded");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error timeout exceeded", ex);
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Client terminated connection! ", ex);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Database connection failed! ", e);
		} catch (InvalidKeyException e) {
			logger.log(Level.SEVERE, "Invalid Key! ", e);
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.SEVERE, "Encryption Algorithm name is not valid! ", e);
		} catch (InvalidKeySpecException e) {
			logger.log(Level.SEVERE, "Invalid Key Specification! ", e);
		}
	}

	/**
	 * Print message to the console
	 */
	private void displayVerboseGreeting() {
		if (verbose) {
			System.out.println("Client Connected");
		}
	}

	/**
	 * Closing the connection between the server and client
	 */
	private void closeConnection() {
		session.close();
		clientsList.remove(session);
		try {
			database.disconnect();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Disconnect from Database failed! ", e);
		}
		if (verbose) {
			System.out.println("Client close connection.");
		}
	}
}
