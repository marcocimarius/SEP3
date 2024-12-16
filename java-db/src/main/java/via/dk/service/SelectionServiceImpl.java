package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.*;
import via.dk.dao.selection.SelectionDao;
import via.dk.dao.selection.SelectionDaoImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for the SelectionService. Based on the SelectionServiceGrpc.SelectionServiceImplBase class.
 * Extends the Grpc-generated SelectionServiceImplBase class.
 */
public class SelectionServiceImpl extends SelectionServiceGrpc.SelectionServiceImplBase
{
  private final SelectionDao selectionDao;

  /**
   * Constructor for the SelectionServiceImpl class.
   */
  public SelectionServiceImpl() {
    this.selectionDao = new SelectionDaoImpl();
  }

  /**
   * Creates a selection.
   * @param request The request to create a selection.
   * @param responseObserver A response observer required by gRPC to send the response.
   */
  @Override
  public void createSelection(CreateSelectionRequest request,
    StreamObserver<CreateSelectionResponse> responseObserver) {
    try {
      int status = this.selectionDao.create(request);
      CreateSelectionResponse.Builder response;
      if (status == 1) {
        response = CreateSelectionResponse.newBuilder()
            .setStatus("Success creating selection");
      }
      else {
        response = CreateSelectionResponse.newBuilder()
            .setStatus("Fail creating selection");
      }
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Updates a selection.
   * @param request The request to update a selection.
   * @param responseObserver A response observer required by gRPC to send the response.
   */
  @Override
  public void updateSelection(UpdateSelectionRequest request,
      StreamObserver<UpdateSelectionResponse> responseObserver) {
    try {
      int status = this.selectionDao.update(request);
      UpdateSelectionResponse.Builder response;
      if (status == 1) {
        response = UpdateSelectionResponse.newBuilder()
            .setStatus("Success updating selection");
      }
      else {
        response = UpdateSelectionResponse.newBuilder()
            .setStatus("Fail updating selection");
      }
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Deletes a selection.
   * @param request The request to delete a selection.
   * @param responseObserver A response observer required by gRPC to send the response.
   */
  @Override
  public void deleteSelection(DeleteSelectionRequest request,
      StreamObserver<DeleteSelectionResponse> responseObserver) {
    try {
      int status = this.selectionDao.delete(request);
      DeleteSelectionResponse.Builder response;
      if (status == 1) {
        response = DeleteSelectionResponse.newBuilder()
            .setStatus("Success deleting selection");
      }
      else {
        response = DeleteSelectionResponse.newBuilder()
            .setStatus("Fail deleting selection");
      }
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /**
   * Retrieves a selection.
   * @param request The request to retrieve a selection.
   * @param responseObserver A response observer required by gRPC to send the response.
   */
  @Override
  public void retrieveSelection(RetrieveSelectionRequest request,
      StreamObserver<RetrieveSelectionResponse> responseObserver)
  {
    try {
      List<Recipe> recipes = this.selectionDao.retrieve(request);
      RetrieveSelectionResponse.Builder response = RetrieveSelectionResponse.newBuilder()
          .addAllRecipes(recipes);
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }
}
