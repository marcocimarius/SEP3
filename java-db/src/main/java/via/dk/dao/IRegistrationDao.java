package via.dk.dao;

import via.dk.model.IRegistration;

import java.sql.SQLException;

public interface IRegistrationDao {
	public int create(IRegistration registration) throws SQLException;
}
