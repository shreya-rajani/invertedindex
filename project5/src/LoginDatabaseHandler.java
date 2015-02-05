import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

//import org.apache.log4j.Logger;

/**
 * Part of the {@link LoginServer} example. Handles all database-related
 * actions.
 * 
 * @author Sophie Engle
 */
public class LoginDatabaseHandler {

	/** A {@link org.apache.log4j.Logger log4j} logger for debugging. */
	// private static Logger log = Logger.getLogger(LoginDatabaseHandler.class);

	/** Makes sure only one database handler is instantiated. */
	private static LoginDatabaseHandler singleton = new LoginDatabaseHandler();

	/** Used to create necessary tables for this example. */
	private static final String CREATE_SQL = "CREATE TABLE login_users ("
			+ "userid INTEGER AUTO_INCREMENT PRIMARY KEY, "
			+ "username VARCHAR(32) NOT NULL UNIQUE, "
			+ "password CHAR(64) NOT NULL, " + "usersalt CHAR(32) NOT NULL);";

	/** Used to create necessary tables for this example. */
	private static final String CREATE_SEARCH_HISTORY_SQL = "CREATE TABLE Search_History ("
			+ "id INTEGER AUTO_INCREMENT PRIMARY KEY, "
			+ "username VARCHAR(32) NOT NULL, "
			+ "searchquery CHAR(30) NOT NULL);";

	/** Used to insert a new user into the database. */
	private static final String REGISTER_SQL = "INSERT INTO login_users (username, password, usersalt) "
			+ "VALUES (?, ?, ?);";

	/** Used to determine if a username already exists. */
	private static final String USER_SQL = "SELECT username FROM login_users WHERE username = ?";

	/** Used to retrieve the salt associated with a specific user. */
	private static final String SALT_SQL = "SELECT usersalt FROM login_users WHERE username = ?";

	/** Used to authenticate a user. */
	private static final String AUTH_SQL = "SELECT username FROM login_users "
			+ "WHERE username = ? AND password = ?";

	private static final String UPDATE_SQL = "UPDATE login_users SET password = ?, usersalt = ? "
			+ "WHERE username = ?;";

	/** Used to create necessary tables for this example. */
	private static final String SEARCH_INSERT_SQL = "INSERT INTO Search_History (username, searchquery) "
			+ "VALUES (?, ?);";

	/** Used to create necessary tables for this example. */
	private static final String GET_HISTORY_SQL = "SELECT DISTINCT(searchquery) FROM Search_History "
			+ "WHERE username = ?;";

	/** Used to create necessary tables for this example. */
	private static final String CLEAR_HISTORY_SQL = "DELETE FROM Search_History "
			+ "WHERE username = ?;";

	/** Used to configure connection to database. */
	private DatabaseConnector db;

	/** Used to generate password hash salt for user. */
	private Random random;

	/**
	 * Initializes a database handler for the Login example. Private constructor
	 * forces all other classes to use singleton.
	 */
	private LoginDatabaseHandler() {

		Status status = Status.OK;
		random = new Random(System.currentTimeMillis());

		try {
			db = new DatabaseConnector();
			status = db.testConnection() != null ? setupTables()
					: Status.CONNECTION_FAILED;
			System.out.println("Connection tested");
		} catch (FileNotFoundException e) {
			status = Status.MISSING_CONFIG;
		} catch (IOException e) {
			status = Status.MISSING_VALUES;
		}

		if (status != Status.OK) {
			// log.fatal(status.message());
			System.exit(-status.ordinal());
		}

		// exit if unable to setup tables necessary for this example
		status = setupTables();
		if (status != Status.OK) {
			// log.fatal(status);
			System.exit(status.ordinal());
		}

		status = setupHistoryTable();
		if (status != Status.OK) {
			// log.fatal(status);
			System.exit(status.ordinal());
		}
	}

	/**
	 * Gets the single instance of the database handler.
	 * 
	 * @return instance of the database handler
	 */
	public static LoginDatabaseHandler getInstance() {
		return singleton;
	}

	/**
	 * Checks to see if a String is null or empty.
	 * 
	 * @param text
	 *            - String to check
	 * @return true if non-null and non-empty
	 */
	public static boolean checkString(String text) {
		return text == null || text.trim().isEmpty();
	}

	/**
	 * Inserts the query into Search_History
	 * 
	 * @param username
	 *            username of the user
	 * @param query
	 *            query word searched for
	 * @return
	 */
	public Status insertSearchHistory(String username, String query) {
		Connection connection = null;
		PreparedStatement statement = null;

		Status status = Status.ERROR;

		// log.debug("Adding " + query + ".");

		try {
			connection = db.getConnection();

			try {
				// insert username, password hash, and salt
				statement = connection.prepareStatement(SEARCH_INSERT_SQL);
				statement.setString(1, username);
				statement.setString(2, query);
				statement.executeUpdate();

				status = Status.OK;
			} catch (SQLException ex) {
				status = Status.SQL_EXCEPTION;

				// log.warn("Unable to insert " + query + ".");
				// log.debug(status, ex);
			}
		} catch (Exception ex) {
			status = Status.CONNECTION_FAILED;
			// log.debug(status, ex);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (Exception ignored) {
				// do nothing
			}
		}
		return status;
	}

	/**
	 * Retrieves search history from the database
	 * 
	 * @param user
	 *            username of the user
	 * @param out
	 *            an instance of PrintWriter
	 */
	public void getHistory(String user, PrintWriter out) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		Status status = Status.ERROR;

		// log.debug("Retrieving Search History...");

		try {
			connection = db.getConnection();

			try {
				statement = connection.prepareStatement(GET_HISTORY_SQL);
				statement.setString(1, user);

				results = statement.executeQuery();
				status = Status.OK;

				try {
					while (results != null && results.next()) {
						out.printf("\t<p>%s</p>",
								results.getString("searchquery"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
					// log.error("Unable to fetch.");
				}

				statement.close();
			} catch (Exception ex) {
				status = Status.SQL_EXCEPTION;
				// log.debug(status, ex);
			}
		} catch (Exception ex) {
			status = Status.CONNECTION_FAILED;
			// log.debug(status, ex);
		} finally {
			try {
				connection.close();
			} catch (Exception ignored) {
				// do nothing
			}
		}
	}

	/**
	 * Clears all queries searched for from the database
	 * 
	 * @param user
	 *            username of the user
	 */
	public void clearHistory(String user) {
		Connection connection = null;
		PreparedStatement statement = null;

		Status status = Status.ERROR;

		// log.debug("Clearing Search History...");

		try {
			connection = db.getConnection();

			try {
				statement = connection.prepareStatement(CLEAR_HISTORY_SQL);
				statement.setString(1, user);

				statement.executeUpdate();
				status = Status.OK;

				statement.close();
			} catch (Exception ex) {
				status = Status.SQL_EXCEPTION;
				// log.debug(status, ex);
			}
		} catch (Exception ex) {
			status = Status.CONNECTION_FAILED;
			// log.debug(status, ex);
		} finally {
			try {
				connection.close();
			} catch (Exception ignored) {
				// do nothing
			}
		}
	}

	/**
	 * Checks if necessary table exists in database, and if not tries to create
	 * it.
	 * 
	 * @return {@link Status.OK} if table exists or create is successful
	 */
	private Status setupTables() {

		Status status = Status.ERROR;

		Connection connection = null;
		Statement statement = null;
		ResultSet results = null;

		try {
			connection = db.getConnection();
			statement = connection.createStatement();
			results = statement.executeQuery("SHOW TABLES LIKE 'login_users';");

			if (!results.next()) {
				// log.debug("Creating tables...");
				statement.executeUpdate(CREATE_SQL);
				results = statement
						.executeQuery("SHOW TABLES LIKE 'login_users';");
				status = (results.next()) ? Status.OK : Status.CREATE_FAILED;
			} else {
				// log.debug("Tables found.");
				status = Status.OK;
			}

			results.close();
			statement.close();
		} catch (Exception ex) {
			status = Status.CREATE_FAILED;
			// log.debug(status, ex);
		} finally {
			// make sure database connection is closed
			try {
				connection.close();
			} catch (Exception ignored) {
			}
		}

		return status;
	}

	/**
	 * Creates table Search_History to store all the search queries queried by
	 * the user
	 * 
	 * @return
	 */
	private Status setupHistoryTable() {

		Connection connection = null;
		Statement statement = null;
		ResultSet results = null;
		Status status = Status.ERROR;

		try {
			connection = db.getConnection();
			statement = connection.createStatement();
			results = statement
					.executeQuery("SHOW TABLES LIKE 'Search_History';");

			if (!results.next()) {
				// attempt to create table
				statement.executeUpdate(CREATE_SEARCH_HISTORY_SQL);

				// check if table now exists
				results = statement
						.executeQuery("SHOW TABLES LIKE 'Search_History';");
				status = (results.next()) ? Status.OK : Status.CREATE_FAILED;
			} else {
				// could check if correct columns here
				status = Status.OK;
			}

			results.close();
			statement.close();
		} catch (Exception ex) {
			status = Status.CREATE_FAILED;
			// log.debug(status, ex);
		} finally {
			try {
				// make sure database connection is closed
				connection.close();
			} catch (Exception ignored) {
				// do nothing
			}
		}
		return status;
	}

	/**
	 * Tests if a user already exists in the database. Requires an active
	 * database connection.
	 * 
	 * @param connection
	 *            - active database connection
	 * @param user
	 *            - username to check
	 * @return Status.OK if user does not exist in database
	 * @throws SQLException
	 */
	private Status duplicateUser(Connection connection, String user) {

		assert connection != null;
		assert user != null;

		Status status = Status.ERROR;

		try {
			PreparedStatement statement = connection.prepareStatement(USER_SQL);
			statement.setString(1, user);

			ResultSet results = statement.executeQuery();
			status = results.next() ? Status.DUPLICATE_USER : Status.OK;
			results.close();
			statement.close();
		} catch (SQLException e) {
			// log.debug(e.getMessage(), e);
			status = Status.SQL_EXCEPTION;
		}

		return status;
	}

	/**
	 * Tests if a user already exists in the database.
	 * 
	 * @see #duplicateUser(Connection, String)
	 * @param user
	 *            - username to check
	 * @return Status.OK if user does not exist in database
	 */
	public Status duplicateUser(String user) {
		Connection connection = null;
		Status status = Status.ERROR;

		try {
			connection = db.getConnection();
			status = duplicateUser(connection, user);
		} catch (SQLException e) {
			status = Status.CONNECTION_FAILED;
			// log.debug(e.getMessage(), e);
		} finally {
			try {
				connection.close();
			} catch (Exception ignored) {
			}
		}

		return status;
	}

	/**
	 * Returns the hex encoding of a byte array.
	 * 
	 * @param bytes
	 *            - byte array to encode
	 * @param length
	 *            - desired length of encoding
	 * @return hex encoded byte array
	 */
	public static String encodeHex(byte[] bytes, int length) {
		BigInteger bigint = new BigInteger(1, bytes);
		String hex = String.format("%0" + length + "X", bigint);

		assert hex.length() == length;
		return hex;
	}

	/**
	 * Calculates the hash of a password and salt using SHA-256.
	 * 
	 * @param password
	 *            - password to hash
	 * @param salt
	 *            - salt associated with user
	 * @return hashed password
	 */
	public static String getHash(String password, String salt) {
		String salted = salt + password;
		String hashed = salted;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salted.getBytes());
			hashed = encodeHex(md.digest(), 64);
		} catch (Exception ex) {
			// log.debug("Unable to properly hash password.", ex);
		}

		return hashed;
	}

	/**
	 * Registers a new user, placing the username, password hash, and salt into
	 * the database if the username does not already exist.
	 * 
	 * @param newuser
	 *            - username of new user
	 * @param newpass
	 *            - password of new user
	 * @return {@link Status.OK} if registration successful
	 */
	private Status registerUser(Connection connection, String newuser,
			String newpass) {

		Status status = Status.ERROR;

		byte[] saltBytes = new byte[16];
		random.nextBytes(saltBytes);

		String usersalt = encodeHex(saltBytes, 32);
		String passhash = getHash(newpass, usersalt);

		try {
			PreparedStatement statement = connection
					.prepareStatement(REGISTER_SQL);
			statement.setString(1, newuser);
			statement.setString(2, passhash);
			statement.setString(3, usersalt);
			statement.executeUpdate();
			statement.close();

			status = Status.OK;
		} catch (SQLException ex) {
			status = Status.SQL_EXCEPTION;
			// log.debug(ex.getMessage(), ex);
		}

		return status;
	}

	/**
	 * Registers a new user, placing the username, password hash, and salt into
	 * the database if the username does not already exist.
	 * 
	 * @param newuser
	 *            - username of new user
	 * @param newpass
	 *            - password of new user
	 * @return {@link Status.OK} if registration successful
	 */
	public Status registerUser(String newuser, String newpass) {
		Connection connection = null;
		Status status = Status.ERROR;

		// log.debug("Registering " + newuser + ".");

		// make sure we have non-null and non-emtpy values for login
		if (checkString(newuser) || checkString(newpass)) {
			status = Status.INVALID_LOGIN;
			// log.debug(status);
			return status;
		}

		// try to connect to database and test for duplicate user
		try {
			connection = db.getConnection();
			status = duplicateUser(connection, newuser);

			// if okay so far, try to insert new user
			if (status == Status.OK) {
				status = registerUser(connection, newuser, newpass);
			}
		} catch (SQLException ex) {
			status = Status.CONNECTION_FAILED;
			// log.debug(status, ex);
		} finally {
			try {
				connection.close();
			} catch (Exception ignored) {
			}
		}

		return status;
	}

	/**
	 * Gets the salt for a specific user.
	 * 
	 * @param connection
	 *            - active database connection
	 * @param user
	 *            - which user to retrieve salt for
	 * @return salt for the specified user or null if user does not exist
	 * @throws SQLException
	 *             if any issues with database connection
	 */
	private String getSalt(Connection connection, String user)
			throws SQLException {
		assert connection != null;
		assert user != null;

		String salt = null;

		PreparedStatement statement = connection.prepareStatement(SALT_SQL);
		statement.setString(1, user);

		ResultSet results = statement.executeQuery();

		if (results.next()) {
			salt = results.getString("usersalt");
		}

		results.close();
		statement.close();

		return salt;
	}

	/**
	 * Checks if the provided username and password match what is stored in the
	 * database. Requires an active database connection.
	 * 
	 * @param username
	 *            - username to authenticate
	 * @param password
	 *            - password to authenticate
	 * @return {@link Status.OK} if authentication successful
	 * @throws SQLException
	 */
	private Status authenticateUser(Connection connection, String username,
			String password) throws SQLException {

		Status status = Status.ERROR;

		try {
			String usersalt = getSalt(connection, username);
			String passhash = getHash(password, usersalt);

			PreparedStatement statement = connection.prepareStatement(AUTH_SQL);
			statement.setString(1, username);
			statement.setString(2, passhash);

			ResultSet results = statement.executeQuery();
			status = results.next() ? status = Status.OK : Status.INVALID_LOGIN;
			results.close();
			statement.close();
		} catch (SQLException e) {
			// log.debug(e.getMessage(), e);
			status = Status.SQL_EXCEPTION;
		}

		return status;
	}

	/**
	 * Checks if the provided username and password match what is stored in the
	 * database. Must retrieve the salt and hash the password to do the
	 * comparison.
	 * 
	 * @param username
	 *            - username to authenticate
	 * @param password
	 *            - password to authenticate
	 * @return {@link Status.OK} if authentication successful
	 */
	public Status authenticateUser(String username, String password) {
		Connection connection = null;
		Status status = Status.ERROR;

		// log.debug("Authenticating user " + username + ".");

		try {
			connection = db.getConnection();
			status = authenticateUser(connection, username, password);
		} catch (SQLException ex) {
			status = Status.CONNECTION_FAILED;
			// log.debug(status, ex);
		} finally {
			try {
				connection.close();
			} catch (Exception ignored) {
			}
		}

		return status;
	}

	Status updateUser(String username, String newPassword) {
		Status status = Status.ERROR;

		// log.debug("Updating user " + username + ".");

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = db.getConnection();

			// create random salt for user
			byte[] saltBytes = new byte[16];
			random.nextBytes(saltBytes);

			String usersalt = encodeHex(saltBytes, 32);
			String passhash = getHash(newPassword, usersalt);

			try {
				statement = connection.prepareStatement(UPDATE_SQL);
				statement.setString(1, passhash);
				statement.setString(2, usersalt);
				statement.setString(3, username);
				statement.executeUpdate();

				status = Status.OK;

				statement.close();
			} catch (Exception ex) {
				status = Status.SQL_EXCEPTION;
				// log.debug(status, ex);
			}
		} catch (Exception ex) {
			status = Status.CONNECTION_FAILED;
			// log.debug(status, ex);
		} finally {
			try {
				// make sure database connection is closed
				connection.close();
			} catch (Exception ignored) {
				// do nothing
			}
		}

		return status;

	}

}