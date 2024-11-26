package via.dk.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static Connection con = null;
	// change based on your database
	private static final String host = System.getenv("db_host");
	private static final String database = System.getenv("db_name");
	private static final String url = "jdbc:postgresql://" + host + "/" + database;
	private static final String user = System.getenv("db_user");
	private static final String password = System.getenv("db_password");

	static {
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("DB Connection failed");
			// close the process
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Connection getConnection() {
		return con;
	}
}
