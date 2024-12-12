package via.dk.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import via.dk.*;
import via.dk.dao.ingredients.IIngredientsDao;
import via.dk.dao.ingredients.IngredientsDaoImpl;
import via.dk.model.ingredient.IngredientModel;

import via.dk.util.TimeConverter;
import via.dk.util.dtos.DeleteIngredientDto;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class IngredientServiceImp extends IngredientsServiceGrpc.IngredientsServiceImplBase
{
  private final IIngredientsDao ingredientsDao;

  public IngredientServiceImp()
  {
    this.ingredientsDao = new IngredientsDaoImpl();
  }

  @Override
  public void createIngredient(CreateIngredientRequest request,
      StreamObserver<CreateIngredientResponse> responseObserver) {
    IngredientModel ingredient = new IngredientModel(null, request.getName(), request.getCalories(), request.getContainsAllergens(), null, null, null);

    try {
      int result = ingredientsDao.createIngredient(ingredient,
          request.getTypeId());

      //this is never gonna happen
      if (result == 0) {
        // If creation failed, throw an exception
        throw new SQLException("FAIL: Ingredient creation failed");
      }

      // If successful, send the response
      CreateIngredientResponse response = CreateIngredientResponse.newBuilder()
          .setStatus("SUCCESS: Creation of ingredient successful")
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (SQLException e) {
      // Log and respond with an INTERNAL gRPC status
      System.err.println("Database error: " + e.getMessage());
      responseObserver.onError(
          Status.INTERNAL
              .withDescription("Database error occurred: " + e.getMessage())
              .asRuntimeException()
      );
    } catch (Exception e) {
      // Handle generic exceptions
      System.err.println("Error occurred: " + e.getMessage());
      responseObserver.onError(
          Status.UNKNOWN
              .withDescription("Unknown error occurred: " + e.getMessage())
              .asRuntimeException()
      );
    }
  }


  @Override
  public void updateIngredient(UpdateIngredientRequest request,
      StreamObserver<UpdateIngredientResponse> responseObserver) {
    IngredientModel ingredient = new IngredientModel(request.getId(),
        request.getName(), request.getCalories(), request.getIsAllergen(),
        new Timestamp(System.currentTimeMillis()), //this is just a placeholder
        new Timestamp(System.currentTimeMillis()),
        " ");
    try {
      int result = ingredientsDao.updateIngredient(ingredient,
          request.getTypeId());
      if (result == 0) {
        throw new Exception("FAIL: Ingredient update failed");
      }
    }
    catch (Exception e) {
      responseObserver.onError(e);
      responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
      return;
    }
    UpdateIngredientResponse response = UpdateIngredientResponse.newBuilder().setStatus("SUCCESS: Update of ingredient successful").build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void deleteIngredient(DeleteIngredientRequest request,
      StreamObserver<DeleteIngredientResponse> responseObserver) {
    DeleteIngredientDto ingredientDto = new DeleteIngredientDto(request.getId());
    try
    {
      int result = ingredientsDao.deleteIngredient(ingredientDto);
      if (result == 0) {
        throw new Exception("FAIL: Ingredient delete failed");
      }
    }
    catch (Exception e) {
      responseObserver.onError(e);
      return;
    }
    DeleteIngredientResponse response = DeleteIngredientResponse.newBuilder().setStatus("SUCCESS: Delete of ingredient successful").build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getAllIngredients(GetAllIngredientsRequest request,
      StreamObserver<GetAllIngredientsResponse> responseObserver) {
    List<IngredientModel> ingredients = new ArrayList<>();
    try {
      ingredients = ingredientsDao.getAllIngredients();
      //might be wrong to throw an exception if there is nothing
      if (ingredients.isEmpty()) throw new Exception("FAIL: There are no recipes");
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
    GetAllIngredientsResponse.Builder response = GetAllIngredientsResponse.newBuilder();
    if (ingredients != null) {
      for (IngredientModel ingredient : ingredients) {
        Ingredient.Builder ingredientBuilder = Ingredient.newBuilder()
            .setId(ingredient.getId())
            .setName(ingredient.getName())
            .setCalories(ingredient.getCalories())
            .setIsAllergen(ingredient.isAllergen())
            .setCreationDate(TimeConverter.toProtobufTimestamp(ingredient.getCreationDate()))
            .setType(ingredient.getType());

        if (ingredient.getModificationDate() != null) {
          ingredientBuilder.setModificationDate(TimeConverter.toProtobufTimestamp(ingredient.getModificationDate()));
        }

        response.addIngredients(ingredientBuilder.build());
      }
    }
    responseObserver.onNext(response.build());
    responseObserver.onCompleted();
  }
}
