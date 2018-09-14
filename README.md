# Mail Retrieval Protocol - Server
The MRP Protocol is basic message retrieval protocol influenced by [IMAP Protocol](https://tools.ietf.org/html/rfc3501) and is written in Java. This protocol is based on **Client-Server** Architecture where a single client or multiple clients connects to the server to retrieve messages which are stored on the server, in a database. To communicate with the MRP Server, a client can be found in [MRP-Client Repository](https://github.com/martinholecekmax/MRP-Client).

## Database installation
The MRP Server connects to a MySQL database which contains stored messages. The SQL file can be imported into the MySQL database which will create a database called **SMTP** and two tables, one for **mailboxes** and the other for **messages**. Mailboxes are user's accounts which allow authentication of a user to retrieve only messages which belong to that user. The MRP Protocol allows creating a new mailbox account, however, there will be no messages for this account unless user will send new messages to that account. For this reason, there is a dummy account created which contains some randomly generated messages that can be fetched by the client. The credentials for connecting to this account are the following:
```ini
User=martin.holecek
Password=university
```
## Connecting to the database
The MRP Server uses [JDBC DriverManager](https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-usagenotes-connect-drivermanager.html) interface which allows connecting into the MySQL database. The class **DBConnect** handles the connection to the database and can be found inside the package called **database**. This class contains two fields, **username** and **password**, which **must** be modified to match up the MySQL account credentials where the database has been imported.
```java
// DBConnect.java file
private final String PASSWORD = "password";
private final String USERNAME = "user";
```
Be aware that these credentials are **not the same** as the previously described credentials stored in database inside the **mailbox table**. The credentials are described here are the creadentials for the **phpMyAdmin account** which serves to control **MySQL server**. The way how to create a MySQL account in phpMyAdmin can be found [here](https://docs.phpmyadmin.net/en/latest/privileges.html).
