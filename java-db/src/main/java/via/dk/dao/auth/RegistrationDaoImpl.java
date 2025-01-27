package via.dk.dao.auth;

import via.dk.model.auth.IRegistration;
import via.dk.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data access object for the Registration object.
 */
public class RegistrationDaoImpl implements IRegistrationDao {
	Connection db = DatabaseConnection.getConnection();

	/**
	 * Method to create a new user.
	 * @param registration The registration object.
	 * @return The number of rows affected.
	 * @throws SQLException
	 */
	@Override
	public int create(IRegistration registration) throws SQLException {
		// insert into registration (username, password, isAdmin) values (registration.getUsername(), registration.getPassword(), registration.getIsAdmin());
		PreparedStatement stmt = db.prepareStatement("insert into registration (username, password, is_admin) values (?, ?, ?)");
		stmt.setString(1, registration.getEmail());
		stmt.setString(2, registration.getPassword());
		stmt.setBoolean(3, registration.getIsAdmin());
		int result = stmt.executeUpdate();
		return result;
	}
}
