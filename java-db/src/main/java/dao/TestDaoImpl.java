package dao;

import model.ITest;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TestDaoImpl implements ITestDao {
	private static final Connection db = DatabaseConnection.getConnection();
	public TestDaoImpl() {
	}

	@Override
	public int add(ITest test) throws SQLException {
		String query = "insert into test (name, description) values (?, ?)";
		PreparedStatement stmt = db.prepareStatement(query);
		stmt.setString(1, test.getName());
		stmt.setString(2, test.getDescription());
		return stmt.executeUpdate();
	}

	@Override
	public ITest getTest(Integer id) throws SQLException {
		return null;
	}

	@Override
	public List<ITest> getTests() throws SQLException {
		return List.of();
	}
}
