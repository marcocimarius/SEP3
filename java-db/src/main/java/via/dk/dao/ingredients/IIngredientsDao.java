package via.dk.dao.ingredients;

import via.dk.model.ingredient.IngredientModel;
import via.dk.util.dtos.DeleteIngredientDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IIngredientsDao
{
  int createIngredient(IngredientModel ingredient, int typeId) throws SQLException;
  int updateIngredient(IngredientModel ingredient, int typeId) throws SQLException;
  int deleteIngredient(DeleteIngredientDto ingredient) throws SQLException;
  List<IngredientModel> getAllIngredients() throws SQLException;
}
