package via.dk.dao.auth;

import via.dk.model.auth.IRegistration;

import java.sql.SQLException;

public interface IRegistrationDao {
	public int create(IRegistration registration) throws SQLException;
}
