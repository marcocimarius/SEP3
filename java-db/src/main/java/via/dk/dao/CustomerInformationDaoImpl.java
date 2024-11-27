package via.dk.dao;

import via.dk.model.auth.ICustomerInformation;
import via.dk.util.DatabaseConnection;

import java.sql.*;

public class CustomerInformationDaoImpl implements ICustomerInformationDao {
	Connection db = DatabaseConnection.getConnection();

	private int getCountryId(String name) throws SQLException{
		PreparedStatement countrySelectStmt = db.prepareStatement("select * from country where name = ?");
		countrySelectStmt.setString(1, name);
		ResultSet rs = countrySelectStmt.executeQuery();
		if(rs.next()){
			return rs.getInt("id");
		}
		return 0;
	}
	private int getCityId(String name) throws SQLException{
		PreparedStatement citySelectStmt = db.prepareStatement("select * from city where name = ?");
		citySelectStmt.setString(1, name);
		ResultSet rs = citySelectStmt.executeQuery();
		if(rs.next()){
			return rs.getInt("id");
		}
		return 0;
	}

	private int getAddressId(String name) throws SQLException{
		PreparedStatement addressSelectStmt = db.prepareStatement("select * from address where street_name = ?");
		addressSelectStmt.setString(1, name);
		ResultSet rs = addressSelectStmt.executeQuery();
		if(rs.next()){
			return rs.getInt("id");
		}
		return 0;
	}

	@Override
	public int create(ICustomerInformation ci) throws SQLException{
		int countryId = getCountryId(ci.getCountryName());
		if(countryId == 0) {
			PreparedStatement countryInsertStmt = db.prepareStatement("insert into country (name) values (?)");
			countryInsertStmt.setString(1, ci.getCountryName());
			countryInsertStmt.executeUpdate();
		}
		countryId = getCountryId(ci.getCountryName());
		int cityId = getCityId(ci.getCityName());
		if(cityId == 0) {
			PreparedStatement cityInsertStmt = db.prepareStatement("insert into city (name, post_number, country_id) values (?, ?, ?)");
			cityInsertStmt.setString(1, ci.getCityName());
			cityInsertStmt.setString(2, ci.getPostNumber());
			cityInsertStmt.setInt(3, countryId);
			cityInsertStmt.executeUpdate();
		}
		cityId = getCityId(ci.getCityName());
		int addressId = getAddressId(ci.getStreetName());
		if(addressId == 0) {
			PreparedStatement addressInsertStmt = db.prepareStatement("insert into address (street_name, number, city_id) values (?, ?, ?)");
			addressInsertStmt.setString(1, ci.getStreetName());
			addressInsertStmt.setString(2, "");
			addressInsertStmt.setInt(3, cityId);
			addressInsertStmt.executeUpdate();
		}
		addressId = getAddressId(ci.getStreetName());
		PreparedStatement customerInsertStmt = db.prepareStatement("insert into customer_information (first_name, last_name, phone, delivery_address_id) values (?, ?, ?, ?)");
		customerInsertStmt.setString(1, ci.getFirstName());
		customerInsertStmt.setString(2, ci.getLastName());
		customerInsertStmt.setString(3, ci.getPhone());
		customerInsertStmt.setInt(4, addressId);
		int result = customerInsertStmt.executeUpdate();
		return result;
	}

	@Override
	public ICustomerInformation get() {
		return null;
	}

	@Override
	public int delete() {
		return 0;
	}

	@Override
	public int edit(ICustomerInformation ci) {
		return 0;
	}
}
