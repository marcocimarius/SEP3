package via.dk.dao.recipe;

import via.dk.CreateRecipeRequest;
import via.dk.model.recipe.Recipe;

import java.sql.SQLException;
import java.util.List;

public interface IRecipeDao
{
  int create(CreateRecipeRequest recipe) throws SQLException;
  List<Recipe> getAllRecipes() throws SQLException;
}
