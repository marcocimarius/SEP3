package via.dk.dao.recipe;

import via.dk.CreateRecipeRequest;
import via.dk.DeleteRecipeRequest;
import via.dk.Recipe;
import via.dk.UpdateRecipeRequest;

import java.sql.SQLException;
import java.util.List;

public interface IRecipeDao
{
  int create(CreateRecipeRequest recipe) throws SQLException;
  List<Recipe> getAllRecipes() throws SQLException;
  int update(UpdateRecipeRequest recipe) throws SQLException;
  int delete(DeleteRecipeRequest recipe) throws SQLException;
}
