package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.*;
import via.dk.dao.subscription.SubscriptionDao;
import via.dk.dao.subscription.SubscriptionDaoImpl;

/**
 * Service class for the SubscriptionService. Based on the SubscriptionServiceGrpc.SubscriptionServiceImplBase class.
 * Extends the Grpc-generated SubscriptionServiceImplBase class.
 */
public class SubscriptionServiceImpl extends SubscriptionServiceGrpc.SubscriptionServiceImplBase
{
  private final SubscriptionDao subscriptionDao;

  /**
   * Constructor for the SubscriptionServiceImpl class.
   */
  public SubscriptionServiceImpl() {
    this.subscriptionDao = new SubscriptionDaoImpl();
  }

  /**
   * Creates a subscription.
   * @param request The request to create a subscription.
   * @param responseObserver A response observer required by gRPC to send the response.
   */
  @Override
  public void createSubscription(CreateSubscriptionRequest request,
      StreamObserver<CreateSubscriptionResponse> responseObserver) {
    try {
      int status = subscriptionDao.createSubscription(request);
      CreateSubscriptionResponse.Builder response;
      if (status == 1) {
        response = CreateSubscriptionResponse.newBuilder().
            setStatus("Success creating subscription");
      }
      else {
        response = CreateSubscriptionResponse.newBuilder().
            setStatus("Fail creating subscription");
      }
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Cancels a subscription.
   * @param request The request to cancel a subscription.
   * @param responseObserver A response observer required by gRPC to send the response.
   */
  @Override
  public void cancelSubscription(CancelSubscriptionRequest request,
      StreamObserver<CancelSubscriptionResponse> responseObserver) {
    try {
      int status = subscriptionDao.cancelSubscription(request);
      CancelSubscriptionResponse.Builder response;
      if (status == 1) {
        response = CancelSubscriptionResponse.newBuilder().
            setStatus("Success cancelling subscription");
      }
      else {
        response = CancelSubscriptionResponse.newBuilder().
            setStatus("Fail cancelling subscription");
      }
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Retrieves a subscription.
   * @param request The request to retrieve a subscription.
   * @param responseObserver A response observer required by gRPC to send the response.
   */
  @Override
  public void retrieveSubscription(RetrieveSubscriptionRequest request,
      StreamObserver<RetrieveSubscriptionResponse> responseObserver) {
    try {
      RetrieveSubscriptionResponse response = subscriptionDao.retrieveSubscription(request);
      if (response == null) {
        response = RetrieveSubscriptionResponse.newBuilder().setId(0).build();
      }
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }
}
