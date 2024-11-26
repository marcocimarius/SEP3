package via.dk.dao;

import via.dk.model.auth.ILogin;
import via.dk.model.auth.Login;
import via.dk.model.auth.Registration;
import via.dk.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDaoImpl implements ILoginDao {
	Connection db = DatabaseConnection.getConnection();

	@Override
	public Registration login(ILogin login) throws SQLException {
		// insert into registration (username, password, isAdmin) values (registration.getUsername(), registration.getPassword(), registration.getIsAdmin());
		PreparedStatement stmt = db.prepareStatement("select * from registration where username = ?");
		stmt.setString(1, login.getEmail());
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			return new Registration(result.getInt("id"),
					result.getString("username"),
					result.getString("password"),
					result.getBoolean("is_admin"));
		}
		return null;
	}
}