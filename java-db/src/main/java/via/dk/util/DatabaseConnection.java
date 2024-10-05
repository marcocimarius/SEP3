package via.dk.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static Connection con = null;
	private static final String url = "jdbc:postgresql://localhost:5432/pro3slaughterhouse";
	private static final String user = "dimitar.nizamov";
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
