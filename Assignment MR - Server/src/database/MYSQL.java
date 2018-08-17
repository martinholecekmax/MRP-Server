package database;

/**
 * This class holds Constants of MYSQL Queries used for selecting, inserting and updating informations inside the database
 * 
 * @author Martin Holecek
 *
 */
public final class MYSQL {	
	public static final String QUERY_SET_INCREMENT = "SET @IncrementValue = 0;";
	public static final String QUERY_MAILBOX_EXISTS = 
			"SELECT * FROM mailboxes WHERE Mailbox = ?";
	public static final String QUERY_CREATE_MAILBOX = 
			"INSERT INTO mailboxes VALUES (NULL, ?, ?, NULL, ?, ?)";
	public static final String QUERY_VALIDATE_MAILBOX = 
			"SELECT * FROM mailboxes WHERE Mailbox = ? AND Password = ?";
	public static final String QUERY_VALIDATE_TOKEN = 
			"SELECT * FROM mailboxes WHERE Mailbox = ? AND Token = ?";
	public static final String QUERY_UPDATE_TOKEN = 
			"UPDATE mailboxes SET Token = ? WHERE Mailbox = ?";
	public static final String QUERY_RESET_UID = 
			"UPDATE messages AS messages " + 
			"INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " + 
			"SET UID = @IncrementValue:=@IncrementValue+1 " + 
			"WHERE mailboxes.Mailbox = ?";	
	public static final String QUERY_MESSAGES = 
			"SELECT MessageID, UID, Subject, Sender, Recipient, Date, Mime, Body, Flag " + 
			"FROM messages INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " + 
			"WHERE mailboxes.Mailbox = ?";
	public static final String QUERY_MESSAGES_UID_SEQUENCE = 
			"SELECT MessageID, UID, Subject, Sender, Recipient, Date, Mime, Body, Flag " + 
			"FROM messages INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " + 
			"WHERE mailboxes.Mailbox = ? AND UID BETWEEN ? AND ?";
	public static final String QUERY_SINGLE_MESSAGE = 
			"SELECT MessageID, UID, Subject, Sender, Recipient, Date, Mime, Body, Flag " + 
			"FROM messages INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " + 
			"WHERE mailboxes.Mailbox = ? AND UID = ?";
	public static final String QUERY_COUNT_MESSAGES = 
			"SELECT COUNT(*) FROM messages " +
			"INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " +
			"WHERE mailboxes.Mailbox = ? AND messages.Flag = ?";
	public static final String QUERY_COUNT_ALL_MESSAGES = 
			"SELECT COUNT(*) FROM messages " +
			"INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " +
			"WHERE mailboxes.Mailbox = ?";
	public static final String QUERY_UPDATE_FLAG = 
			"UPDATE messages AS messages " + 
			"INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " + 
			"SET messages.Flag = ? " + 
			"WHERE messages.messageID = ? AND mailboxes.Mailbox = ?";
	public static final String QUERY_DELETE_MESSAGES = 
			"DELETE messages FROM messages " +
			"INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " +
			"WHERE mailboxes.Mailbox = ? AND Flag = 'DELETED'";
	public static final String QUERY_SEARCH_ALL = 
			"SELECT MessageID, UID, Subject, Sender, Recipient, Date, Mime, Body, Flag " + 
			"FROM messages INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " + 
			"WHERE mailboxes.Mailbox = ? AND (Subject LIKE ? OR Sender LIKE ? OR Recipient LIKE ? " +
			"OR Body LIKE ?)";
	public static final String QUERY_DATE_SINCE = 
			"SELECT MessageID, UID, Subject, Sender, Recipient, Date, Mime, Body, Flag " + 
			"FROM messages INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " + 
			"WHERE mailboxes.Mailbox = ? AND Date >= ? ORDER BY UID ASC";
	public static final String QUERY_DATE_UNTIL = 
			"SELECT MessageID, UID, Subject, Sender, Recipient, Date, Mime, Body, Flag " + 
			"FROM messages INNER JOIN mailboxes ON messages.MailboxID = mailboxes.MailboxID " + 
			"WHERE mailboxes.Mailbox = ? AND Date <= ? ORDER BY UID ASC";
}
