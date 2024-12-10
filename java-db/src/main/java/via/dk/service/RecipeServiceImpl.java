package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.*;
import via.dk.dao.recipe.IRecipeDao;
import via.dk.dao.recipe.RecipeDaoImpl;
import via.dk.util.TimeConverter;

import java.util.ArrayList;
import java.util.List;

public class RecipeServiceImpl extends RecipeServiceGrpc.RecipeServiceImplBase
{
  private final IRecipeDao recipeDao;

  public RecipeServiceImpl() {
    this.recipeDao = new RecipeDaoImpl();
  }

  @Override
  public void createRecipe(CreateRecipeRequest request,
      StreamObserver<CreateRecipeResponse> responseObserver) {
    int status;
    try {
      status = recipeDao.create(request);
      if (status == 0) {
        throw new Exception("Fail creating the recipe itself, without adding ingredients");
      }
      else if (status == -1)
      {
        throw new Exception("Fail assigning ingredients to the recipe");
      }
      else {
        CreateRecipeResponse.Builder response = CreateRecipeResponse.newBuilder().setStatus("SUCCESS. Recipe created");
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
      }
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void getAllRecipes(RetrieveRecipeRequest request,
      StreamObserver<RetrieveRecipeResponse> responseObserver) {
    List<Recipe> recipes = new ArrayList<>();
    try {
      recipes = recipeDao.getAllRecipes();
      if (recipes.isEmpty()) {
        //maybe the message structure should be restructured so that it supports a status message in case there are no recipes and then print this out on the site
        throw new Exception("FAIL: There are no recipes");
      }
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
    RetrieveRecipeResponse.Builder response = RetrieveRecipeResponse.newBuilder();
    if (recipes != null) {
      for (Recipe recipe : recipes)
      {
        via.dk.Recipe.Builder recipeBuilder = via.dk.Recipe.newBuilder()
            .setId(recipe.getId())
            .setName(recipe.getName())
            .setType(recipe.getType())
            .setContainsAllergen(recipe.getContainsAllergen())
            .setCalories(recipe.getCalories())
            .setCreationDate(recipe.getCreationDate())
            .setDescription(recipe.getDescription());

        if (recipe.hasModificationDate()) {
          recipeBuilder.setModificationDate(recipe.getModificationDate());
        }
        recipeBuilder.setImageLink(recipe.getImageLink());
        recipeBuilder.addAllIngredients(recipe.getIngredientsList());

        response.addRecipes(recipeBuilder.build());
      }
    }
    responseObserver.onNext(response.build());
    responseObserver.onCompleted();
  }

  @Override
  public void updateRecipe(UpdateRecipeRequest request,
      StreamObserver<UpdateRecipeResponse> responseObserver) {
    try {
      int status = recipeDao.update(request);
      if (status == 0) {
        throw new Exception("FAIL: Could not update the recipe itself, without the ingredients");
      }
      else if (status == -1)
      {
        throw new Exception("FAIL: Could not update the ingredients of the recipe");
      }
      else {
        UpdateRecipeResponse.Builder response = UpdateRecipeResponse.newBuilder().setStatus("SUCCESS updating the recipe");
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
      }
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void deleteRecipe(DeleteRecipeRequest request,
      StreamObserver<DeleteRecipeResponse> responseObserver) {
    try {
      int status = recipeDao.delete(request);
      if (status == 0) {
        throw new Exception("FAIL: Could not delete the recipe itself");
      }
      else if (status == -1)
      {
        throw new Exception("FAIL: Could not delete the ingredients of the recipe");
      }
      else {
        DeleteRecipeResponse.Builder response = DeleteRecipeResponse.newBuilder().setStatus("SUCCESS deleting the recipe");
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
      }
    }
    catch (Exception e) {
      responseObserver.onError(e);
    }
  }
}
