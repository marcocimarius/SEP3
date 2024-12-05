package via.dk.dao.subscription;

import via.dk.CancelSubscriptionRequest;
import via.dk.CreateSubscriptionRequest;
import via.dk.RetrieveSubscriptionRequest;
import via.dk.RetrieveSubscriptionResponse;

import java.sql.SQLException;

public interface SubscriptionDao
{
  int createSubscription(CreateSubscriptionRequest request) throws SQLException;
  int cancelSubscription(CancelSubscriptionRequest request) throws SQLException;
  RetrieveSubscriptionResponse retrieveSubscription(RetrieveSubscriptionRequest request) throws SQLException;
}
