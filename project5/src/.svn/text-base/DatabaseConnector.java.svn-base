import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * Prof. Engle
 * This class is designed to test your database configuration. You need to have
 * a database.properties file with username, password, database, and hostname.
 * You must also have the tunnel to stargate.cs.usfca.edu running.
 */
public class DatabaseConnector {

	/** URI to use when connecting to database. */
	private final String dbURI;

	/** Properties with username and password for connecting to database. */
	private final Properties dbLogin;

	/**
	 * Creates a connector from a "database.properties" file located in the
	 * current working directory.
	 *
	 * @throws IOException if unable to properly parse properties file
	 * @throws FileNotFoundException if properties file not found
	 */
	public DatabaseConnector() throws FileNotFoundException, IOException {
		this("database.properties");
	}

	/**
	 * Creates a connector from the provided database properties file.
	 *
	 * @param configPath path to the database properties file
	 * @throws IOException if unable to properly parse properties file
	 * @throws FileNotFoundException if properties file not found
	 */
	public DatabaseConnector(String configPath) throws FileNotFoundException,
			IOException {

		Properties config = loadConfig(configPath);

		dbURI = String.format("jdbc:mysql://%s/%s",
				config.getProperty("hostname"),
				config.getProperty("database"));

		dbLogin = new Properties();
		dbLogin.put("user", config.getProperty("username"));
		dbLogin.put("password", config.getProperty("password"));
	}

	/**
	 * Attempts to load properties file with database configuration. Must
	 * include username, password, database, and hostname.
	 *
	 * @param configPath path to database properties file
	 * @return database properties
	 * @throws IOException if unable to properly parse properties file
	 * @throws FileNotFoundException if properties file not found
	 */
	private Properties loadConfig(String configPath)
			throws FileNotFoundException, IOException {

		List<String> keys = Arrays.asList(new String[] { "username",
				"password", "database", "hostname" });

		Properties config = new Properties();
		config.load(new FileReader(configPath));

		if (!config.keySet().containsAll(keys)) {
			String error = "Provide following in properties file: ";
			throw new InvalidPropertiesFormatException(error + keys);
		}

		return config;
	}

	/**
	 * Attempts to connect to database using loaded configuration.
	 *
	 * @return database connection
	 * @throws SQLException if unable to establish database connection
	 */
	public Connection getConnection() throws SQLException {
		Connection dbConnection = DriverManager.getConnection(dbURI, dbLogin);
		dbConnection.setAutoCommit(true);
		return dbConnection;
	}

	/**
	 * Executes a SQL statement on an already existing database connection.
	 *
	 * @param db open database connection
	 * @param sql statement to execute
	 * @return already-executed statement
	 * @throws SQLException
	 */
	public Statement executeSQL(Connection db, String sql) throws SQLException {
		Statement statement = db.createStatement();
		statement.execute(sql);
		return statement;
	}

	/**
	 * Opens a database connection, executes a simple statement, and closes
	 * the database connection.
	 *
	 * @return true if all operations successful
	 */
	public Status testConnection() {

		Connection db = null;
		Statement sql = null;
		ResultSet results = null;

		Status status = Status.ERROR;

		try {
			System.out.println("Connecting to " + dbURI);
			db = getConnection();

			sql = executeSQL(db, "SHOW DATABASES;");

			results = sql.getResultSet();

			while (results.next()) {
				System.out.println("Found: " + results.getString("Database"));
			}

			sql.close();
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		finally {
			try {
				db.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}

		return status;
	}

	/**
	 * Tests whether database configuration (including tunnel) is correct.
	 * @param args unused
	 */
	public static void main(String[] args) {
		try {
			DatabaseConnector test = new DatabaseConnector("database.properties");

			if (test.testConnection() != null) {
				System.out.println("Connection to database established.");
			}
			else {
				System.err.println("Unable to connect properly to database.");
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
