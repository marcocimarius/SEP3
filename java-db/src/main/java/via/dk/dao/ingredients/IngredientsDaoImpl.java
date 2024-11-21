package via.dk.dao.ingredients;

import via.dk.model.ingredient.IngredientModel;
import via.dk.util.DatabaseConnection;
import via.dk.util.dtos.DeleteIngredientDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientsDaoImpl implements IIngredientsDao
{
  private final Connection db = DatabaseConnection.getConnection();

  @Override public int createIngredient(IngredientModel ingredient)
      throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("""
        insert into ingredient (name, calories, is_allergen)
        values (?, ?, ?)
        """);
    statement.setString(1, ingredient.getName());
    statement.setInt(2, ingredient.getCalories());
    statement.setBoolean(3, ingredient.isAllergen());
    return statement.executeUpdate();
  }

  @Override public int updateIngredient(IngredientModel ingredient)
      throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("""
        update ingredient
        set name = ?, calories = ?, is_allergen = ?, modification_date = ?
        where id = ?
        """);
    statement.setString(1, ingredient.getName());
    statement.setInt(2, ingredient.getCalories());
    statement.setBoolean(3, ingredient.isAllergen());
    statement.setDate(4, new Date(ingredient.getModificationDate().getTime()));
    statement.setInt(5, ingredient.getId());
    return statement.executeUpdate();
  }

  @Override public int deleteIngredient(DeleteIngredientDto ingredient)
      throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("""
        delete from ingredient where id = ?
        """);
    statement.setInt(1, ingredient.getId());
    return statement.executeUpdate();
  }

  @Override public List<IngredientModel> getAllIngredients() throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("select * from ingredient");
    ResultSet resultSet = statement.executeQuery();
    List<IngredientModel> ingredients = new ArrayList<>();
    while (resultSet.next()) {
      ingredients.add(new IngredientModel(resultSet.getInt("id"),
          resultSet.getString("name"),
          resultSet.getInt("calories"),
          resultSet.getBoolean("is_allergen"),
          resultSet.getTimestamp("creation_date"),
          resultSet.getTimestamp("modification_date")));
    }
    return ingredients;
  }
}
