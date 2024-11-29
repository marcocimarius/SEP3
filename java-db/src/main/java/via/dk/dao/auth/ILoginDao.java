package via.dk.dao.auth;

import via.dk.model.auth.ILogin;
import via.dk.model.auth.Login;
import via.dk.model.auth.Registration;

import java.sql.SQLException;

public interface ILoginDao {
	public Registration login(ILogin login) throws SQLException;
}
