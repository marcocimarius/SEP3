package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.*;
import via.dk.dao.recipe.IRecipeDao;
import via.dk.dao.recipe.RecipeDaoImpl;
import via.dk.model.recipe.Recipe;
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
    //needs more work
  }

  @Override
  public void getAllRecipes(RetrieveRecipeRequest request,
      StreamObserver<RetrieveRecipeResponse> responseObserver) {
    List<Recipe> recipes = new ArrayList<>();
    try {
      recipes = recipeDao.getAllRecipes();
      if (recipes == null) {
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
            .setContainsAllergen(recipe.isContainsAllergen())
            .setCalories(recipe.getCalories())
            .setCreationDate(TimeConverter.toProtobufTimestamp(recipe.getCreationDate()));

        if (recipe.getModificationDate() != null) {
          recipeBuilder.setModificationDate(TimeConverter.toProtobufTimestamp(recipe.getModificationDate()));
        }

        recipeBuilder.setImageLink(recipe.getImageLink() == null || recipe.getImageLink().isEmpty() ? "" : recipe.getImageLink());

        response.addRecipes(recipeBuilder.build());
      }
    }
    responseObserver.onNext(response.build());
    responseObserver.onCompleted();
  }
}