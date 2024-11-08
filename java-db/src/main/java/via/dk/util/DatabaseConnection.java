package via.dk.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static Connection con = null;
	// change based on your database
	private static final String url = "jdbc:postgresql://localhost:5432/goodbyerotten";
	private static final String user = "postgres";
	private static final String password = "";

	static
	{
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("DB Connection failed");
			e.printStackTrace();
		}
	}
	public static Connection getConnection()
	{
		return con;
	}
}
