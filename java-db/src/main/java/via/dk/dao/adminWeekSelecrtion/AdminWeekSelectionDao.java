package via.dk.dao.adminWeekSelecrtion;

import via.dk.*;

import java.sql.SQLException;
import java.util.List;

public interface AdminWeekSelectionDao
{
  int createAdminWeekSelection(CreateAdminWeekSelectionRequest request) throws SQLException;
  int deleteAdminWeekSelection(DeleteAdminWeekSelectionRequest request) throws SQLException;
  int updateAdminWeekSelection(UpdateAdminWeekSelectionRequest request) throws SQLException;
  List<AdminWeek> retrieveAdminWeekSelection(RetrieveAdminWeekSelectionRequest request) throws SQLException;
}
