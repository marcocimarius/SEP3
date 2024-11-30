package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.*;
import via.dk.dao.adminWeekSelecrtion.AdminWeekSelectionDao;
import via.dk.dao.adminWeekSelecrtion.AdminWeekSelectionImpl;

import java.util.List;

public class AdminWeekSelectionServiceImpl extends AdminWeekSelectionServiceGrpc.AdminWeekSelectionServiceImplBase
{
  private final AdminWeekSelectionDao adminWeekSelectionDao;

  public AdminWeekSelectionServiceImpl()
  {
    this.adminWeekSelectionDao = new AdminWeekSelectionImpl();
  }

  @Override
  public void createAdminWeekSelection(CreateAdminWeekSelectionRequest request,
      StreamObserver<CreateAdminSelectionResponse> responseObserver) {
    try {
      int status = this.adminWeekSelectionDao.createAdminWeekSelection(request);
      CreateAdminSelectionResponse.Builder response;
      if (status == 1) {
        response = CreateAdminSelectionResponse.newBuilder()
            .setStatus("Success creating admin week");
      }
      else {
        response = CreateAdminSelectionResponse.newBuilder()
            .setStatus("Fail creating admin week");
      }
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteAdminWeekSelection(DeleteAdminWeekSelectionRequest request,
      StreamObserver<DeleteAdminWeekSelectionResponse> responseObserver) {
    try {
      int status = this.adminWeekSelectionDao.deleteAdminWeekSelection(request);
      DeleteAdminWeekSelectionResponse.Builder response;
      if (status == 1) {
        response = DeleteAdminWeekSelectionResponse.newBuilder()
            .setStatus("Success deleting admin week");
      }
      else {
        response = DeleteAdminWeekSelectionResponse.newBuilder()
            .setStatus("Fail deleting admin week");
      }
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void updateAdminWeekSelection(UpdateAdminWeekSelectionRequest request,
      StreamObserver<UpdateAdminWeekSelectionResponse> responseObserver) {
    try {
      int status = this.adminWeekSelectionDao.updateAdminWeekSelection(request);
      UpdateAdminWeekSelectionResponse.Builder response;
      if (status == 1) {
        response = UpdateAdminWeekSelectionResponse.newBuilder()
            .setStatus("Success updating admin week");
      }
      else {
        response = UpdateAdminWeekSelectionResponse.newBuilder()
            .setStatus("Fail updating admin week");
      }
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void retrieveAdminWeekSelection(RetrieveAdminWeekSelectionRequest request,
      StreamObserver<RetrieveAdminWeekSelectionResponse> responseObserver) {
    try {
      List<AdminWeek> adminWeeks = this.adminWeekSelectionDao.retrieveAdminWeekSelection(request);
      RetrieveAdminWeekSelectionResponse.Builder response = RetrieveAdminWeekSelectionResponse.newBuilder()
          .addAllAdminWeeks(adminWeeks);
      responseObserver.onNext(response.build());
      responseObserver.onCompleted();
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }

  }
}
