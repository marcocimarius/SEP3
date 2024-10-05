package via.dk.dao;

import via.dk.model.ITest;

import java.sql.SQLException;
import java.util.List;

public interface ITestDao {
		public int add(ITest test) throws SQLException;
		public ITest getTest(Integer id) throws SQLException;
		public List<ITest> getTests() throws SQLException;
}
