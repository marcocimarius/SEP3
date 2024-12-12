package via.dk.dao.selection;

import via.dk.*;

import java.sql.SQLException;
import java.util.List;

public interface SelectionDao
{
  int create(CreateSelectionRequest request) throws SQLException;
  int update(UpdateSelectionRequest request) throws SQLException;
  int delete(DeleteSelectionRequest request) throws SQLException;
  List<Recipe> retrieve(RetrieveSelectionRequest request) throws SQLException;
}
