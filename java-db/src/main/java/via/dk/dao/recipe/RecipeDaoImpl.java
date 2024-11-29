package via.dk.dao.recipe;

import via.dk.CreateRecipeRequest;
import via.dk.DeleteRecipeRequest;
import via.dk.UpdateRecipeRequest;
import via.dk.model.ingredient.IngredientModel;
import via.dk.model.recipe.Recipe;
import via.dk.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDaoImpl implements IRecipeDao
{
  private final Connection db = DatabaseConnection.getConnection();

  @Override public int create(CreateRecipeRequest recipe) throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("""
        insert into recipe (name, image_link) values (?, ?)
        returning id
        """);
    statement.setString(1, recipe.getName());
    statement.setString(2, recipe.getImageLink());
    ResultSet resultSet = statement.executeQuery();
    int newRecipeId = 0;
    if (resultSet.next()) {
      newRecipeId = resultSet.getInt("id");
      PreparedStatement recipeWithIngredientsStatement = db.prepareStatement(
          """
              insert into recipes_with_ingredients (recipe_id, ingredient_id)
              values (?, ?)
              """);

      for (int i = 0; i < recipe.getIngredientsIdList().size(); i++) {
        recipeWithIngredientsStatement.setInt(1, newRecipeId);
        recipeWithIngredientsStatement.setInt(2, recipe.getIngredientsIdList().get(i));
        recipeWithIngredientsStatement.addBatch();
      }

      int[] results2 = recipeWithIngredientsStatement.executeBatch();
      for (int result : results2) {
        if (result == -3) {
          return -1;
        }
      }

      return 1;
    }

    return 0;
  }

  @Override public List<Recipe> getAllRecipes() throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("select * from recipe");
    ResultSet resultSet = statement.executeQuery();
    List<Recipe> recipes = new ArrayList<>();

    if (!resultSet.wasNull()) {
      while (resultSet.next()) {
        int recipeId = resultSet.getInt("id");
        PreparedStatement recipeStatement = db.prepareStatement("""
          select ingredient.*, type.type from ingredient
          inner join recipes_with_ingredients on ingredient.id = recipes_with_ingredients.ingredient_id
          inner join ingredient_type on ingredient.id = ingredient_type.ingredient_id
          inner join type on ingredient_type.type_id = type.id
          where recipes_with_ingredients.recipe_id = ?
          group by ingredient.id, type.type
          order by id
          """);

        List<IngredientModel> ingredients = new ArrayList<>();
        recipeStatement.setInt(1, recipeId);
        ResultSet ingredientsResultSet = recipeStatement.executeQuery();
        while (ingredientsResultSet.next()) {
          if (!ingredientsResultSet.wasNull()) {
            int id = ingredientsResultSet.getInt("id");
            String name = ingredientsResultSet.getString("name");
            int calories = ingredientsResultSet.getInt("calories");
            boolean isAllergen = ingredientsResultSet.getBoolean("is_allergen");
            Timestamp creationDate = ingredientsResultSet.getTimestamp("creation_date");
            Timestamp modificationDate = ingredientsResultSet.getTimestamp("modification_date");
            String type = ingredientsResultSet.getString("type");
            ingredients.add(new IngredientModel(id, name, calories, isAllergen,
                creationDate, modificationDate, type));
          }
        }

        String recipeName = resultSet.getString("name");
        String type = resultSet.getString("type");
        boolean containsAllergen = resultSet.getBoolean("contains_allergen");
        int calories = resultSet.getInt("calories");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        Timestamp modificationDate = resultSet.getTimestamp("modification_date");
        String imageLink = resultSet.getString("image_link");
        recipes.add(new Recipe(recipeId, recipeName, type, containsAllergen, calories, creationDate, modificationDate, imageLink, ingredients));
      }
    }

    return recipes;
  }

  @Override public int update(UpdateRecipeRequest recipe) throws SQLException
  {
    int status = 0;
    PreparedStatement statement = db.prepareStatement("""
        update recipe \s
        set name = ?,
            type = ?,
            contains_allergen = ?,
            calories = ?,
            image_link = ?
            where id = ?
        """);
    statement.setString(1, recipe.getName());
    statement.setString(2, "vegan");
    statement.setBoolean(3, false);
    statement.setInt(4, 0);
    statement.setString(5, recipe.getImageLink());
    statement.setInt(6, recipe.getId());

    status = statement.executeUpdate();
    boolean allSuccess = true;
    if (status != 0) {
      statement = db.prepareStatement("""
          delete from recipes_with_ingredients \s
          where recipe_id = ?
          """);
      statement.setInt(1, recipe.getId());
      status = statement.executeUpdate();
      if (status != 0) {
        statement = db.prepareStatement("""
              insert into recipes_with_ingredients (recipe_id, ingredient_id)
              values (?, ?)
              """);
        for (int i = 0; i < recipe.getIngredientsIdList().size(); i++)
        {
          statement.setInt(1, recipe.getId());
          statement.setInt(2, recipe.getIngredientsIdList().get(i));
          statement.addBatch();
        }
      }
      else {
        return 0;
      }


      int[] results = statement.executeBatch();
      for (int result : results) {
        if (result < 0) {
          allSuccess = false;
          break;
        }
      }
    }
    else {
      return status;
    }

    return allSuccess ? 1 : -1;
  }

  @Override public int delete(DeleteRecipeRequest recipe) throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("""
        delete from recipes_with_ingredients where recipe_id = ?
        """);
    statement.setInt(1, recipe.getId());
    statement.executeUpdate();

    statement = db.prepareStatement("""
         delete from recipe where id = ?
    """);
    statement.setInt(1, recipe.getId());

    if (statement.executeUpdate() == 0) {
      return 0;
    }
    else {
      return 1;
    }
  }
}
