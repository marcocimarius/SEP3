package via.dk.dao.subscription;

import via.dk.CancelSubscriptionRequest;
import via.dk.CreateSubscriptionRequest;
import via.dk.RetrieveSubscriptionRequest;
import via.dk.RetrieveSubscriptionResponse;
import via.dk.util.DatabaseConnection;
import via.dk.util.TimeConverter;

import java.sql.*;

public class SubscriptionDaoImpl implements SubscriptionDao
{
  private final Connection db = DatabaseConnection.getConnection();

  @Override public int createSubscription(CreateSubscriptionRequest request) throws SQLException
  {
    try {
      PreparedStatement statement = db.prepareStatement("""
          select * from subscription where registration_id = ?
          """);
      statement.setInt(1, request.getRegistrationId());
      ResultSet resultSet = statement.executeQuery();
      if (!resultSet.isBeforeFirst()) {
        statement = db.prepareStatement("""
          INSERT INTO subscription (registration_id) VALUES (?)
          """);
        statement.setInt(1, request.getRegistrationId());

        if (statement.executeUpdate() != 0) {
          return 1;
        }
      }
    }
    catch (SQLException e) {
      System.err.println("SQL Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    catch (Exception e) {
      System.err.println("Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    return 0;
  }

  @Override public int cancelSubscription(CancelSubscriptionRequest request) throws SQLException
  {
    try {
      PreparedStatement statement = db.prepareStatement("""
          update subscription
          set cancel_date = ?
          where id = ?
          """);
      statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
      statement.setInt(2, request.getId());

      if (statement.executeUpdate() != 0) {
        return 1;
      }
    }
    catch (SQLException e) {
      System.err.println("SQL Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    catch (Exception e) {
      System.err.println("Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    return 0;
  }

  @Override public RetrieveSubscriptionResponse retrieveSubscription(
      RetrieveSubscriptionRequest request) throws SQLException
  {
    try {
      PreparedStatement statement = db.prepareStatement("""
          select * from subscription where registration_id = ?
          """);
      statement.setInt(1, request.getRegistrationId());

      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        int id = resultSet.getInt("id");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        Timestamp cancelDate = resultSet.getTimestamp("cancel_date");


        RetrieveSubscriptionResponse.Builder response = RetrieveSubscriptionResponse.newBuilder()
            .setId(id)
            .setRegistrationId(request.getRegistrationId())
            .setCreationDate(TimeConverter.toProtobufTimestamp(creationDate));

        if (cancelDate != null) {
          response.setCancelDate(TimeConverter.toProtobufTimestamp(cancelDate));
        }

        response.build();

        return response.build();
      }
    }
    catch (SQLException e) {
      System.err.println("SQL Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    catch (Exception e) {
      System.err.println("Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    return null;
  }
}
