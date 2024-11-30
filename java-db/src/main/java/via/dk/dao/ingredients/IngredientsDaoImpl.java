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

  @Override public int createIngredient(IngredientModel ingredient, int typeId)
      throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("""
        insert into ingredient (name, calories, is_allergen)
        values (?, ?, ?)
        """);
    statement.setString(1, ingredient.getName());
    statement.setInt(2, ingredient.getCalories());
    statement.setBoolean(3, ingredient.isAllergen());
    if (statement.executeUpdate() != 0) {
      statement = db.prepareStatement("""
          select id from ingredient where name = ? and calories = ? and is_allergen = ?
          """);
      statement.setString(1, ingredient.getName());
      statement.setInt(2, ingredient.getCalories());
      statement.setBoolean(3, ingredient.isAllergen());
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        int ingredientId = resultSet.getInt("id");
        statement = db.prepareStatement("""
            insert into ingredient_type (type_id, ingredient_id)
            values (?, ?)
            """);

        statement.setInt(1, typeId);
        statement.setInt(2, ingredientId);
        if (statement.executeUpdate() == 0)
        {
          throw new SQLException(
              "Failed to create the ingredient_type table on type_id = "
                  + typeId + " and ingredient_id = " + ingredientId);
        }
      }
    }
    else {
      throw new SQLException("Error creating in ingredient table. Content:\n"
          + "1) id = " + ingredient.getId() + "\n"
          + "2) name = " + ingredient.getName() + "\n"
          + "3) calories = " + ingredient.getCalories() + "\n"
          + "4) isAllergen = " + ingredient.isAllergen()
      );
    }
    return 1;
  }

  @Override public int updateIngredient(IngredientModel ingredient, int typeId)
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
    if (statement.executeUpdate() != 0) {
      statement = db.prepareStatement("""
          update ingredient_type set type_id = ? where ingredient_id = ?
          """);
      statement.setInt(1, typeId);
      statement.setInt(2, ingredient.getId());

      if (statement.executeUpdate() == 0) {
        throw new SQLException("Failed to update the ingredient_type table on type_id = " + typeId + " and ingredient_id = " + ingredient.getId());
      }
    }
    else {
      throw new SQLException("Failed to update the ingredient table. Content:\n "
          + "1) id = " + ingredient.getId() + "\n"
          + "2) name = " + ingredient.getName() + "\n"
          + "3) calories = " + ingredient.getCalories() + "\n"
          + "4) isAllergen = " + ingredient.isAllergen());
    }

    return 1;
  }

  @Override public int deleteIngredient(DeleteIngredientDto ingredient)
      throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("""
        delete from ingredient_type where ingredient_id = ?
        """);
    statement.setInt(1, ingredient.getId());
    //THIS WILL NOT WORK IF THE INGREDIENT DOES NOT HAVE ANY TYPES BOUND TO IT
    if (statement.executeUpdate() != 0) {
      statement = db.prepareStatement("""
          delete from ingredient where id = ?
          """);
      statement.setInt(1, ingredient.getId());
      if (statement.executeUpdate() == 0) {
        throw new SQLException("Error on deleting from ingredient table on id = " + ingredient.getId());
      }
    }
    else {
      throw new SQLException("Error on deleting from ingredient_table table on id = " + ingredient.getId());
    }
    return 1;
  }

  @Override public List<IngredientModel> getAllIngredients() throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("""
        select i.*, type.type from ingredient i \s
        inner join ingredient_type on i.id = ingredient_type.ingredient_id \s
        inner join type on ingredient_type.type_id = type.id;
        """);
    ResultSet resultSet = statement.executeQuery();
    List<IngredientModel> ingredients = new ArrayList<>();
    while (resultSet.next()) {
      ingredients.add(new IngredientModel(resultSet.getInt("id"),
          resultSet.getString("name"),
          resultSet.getInt("calories"),
          resultSet.getBoolean("is_allergen"),
          resultSet.getTimestamp("creation_date"),
          resultSet.getTimestamp("modification_date"),
          resultSet.getString("type")));
    }
    return ingredients;
  }
}
