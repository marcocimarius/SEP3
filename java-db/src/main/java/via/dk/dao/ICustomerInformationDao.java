package via.dk.dao;

import via.dk.model.auth.ICustomerInformation;

import java.sql.SQLException;

public interface ICustomerInformationDao {
	public int create(ICustomerInformation ci) throws SQLException;
	public ICustomerInformation get() throws SQLException;
	public int delete() throws SQLException;
	public int edit(ICustomerInformation ci) throws SQLException;
}
