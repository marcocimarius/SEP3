package via.dk.dao.recipe;

import via.dk.*;
import via.dk.util.DatabaseConnection;
import via.dk.util.TimeConverter;

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

    try {
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

        List<Ingredient> ingredients = new ArrayList<>();
        recipeStatement.setInt(1, recipeId);
        ResultSet ingredientsResultSet = recipeStatement.executeQuery();
        while (ingredientsResultSet.next()) {
          int id = ingredientsResultSet.getInt("id");
          String name = ingredientsResultSet.getString("name");
          int calories = ingredientsResultSet.getInt("calories");
          boolean isAllergen = ingredientsResultSet.getBoolean("is_allergen");
          Timestamp creationDate = ingredientsResultSet.getTimestamp("creation_date");
          Timestamp modificationDate = ingredientsResultSet.getTimestamp("modification_date");
          String type = ingredientsResultSet.getString("type");
          Ingredient.Builder ingredient = Ingredient.newBuilder()
              .setId(id)
              .setName(name)
              .setCalories(calories)
              .setIsAllergen(isAllergen)
              .setCreationDate(TimeConverter.toProtobufTimestamp(creationDate))
              .setType(type);
          if (TimeConverter.toProtobufTimestamp(modificationDate) != null) {
            ingredient.setModificationDate(TimeConverter.toProtobufTimestamp(modificationDate));
          }
          ingredient.build();
          ingredients.add(ingredient.build());
        }

        String recipeName = resultSet.getString("name");
        String type = resultSet.getString("type");
        boolean containsAllergen = resultSet.getBoolean("contains_allergen");
        int calories = resultSet.getInt("calories");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        Timestamp modificationDate = resultSet.getTimestamp("modification_date");
        String imageLink = resultSet.getString("image_link");
        Recipe.Builder recipe = Recipe.newBuilder()
            .setId(recipeId)
            .setName(recipeName)
            .setType(type)
            .setContainsAllergen(containsAllergen)
            .setCalories(calories)
            .setCreationDate(TimeConverter.toProtobufTimestamp(creationDate))
            .setImageLink(imageLink == null ? " " : imageLink)
            .addAllIngredients(ingredients);
        if (TimeConverter.toProtobufTimestamp(modificationDate) != null) {
          recipe.setModificationDate(TimeConverter.toProtobufTimestamp(modificationDate));
        }
        recipe.build();
        recipes.add(recipe.build());
      }
    }
    catch (SQLException e) {
      System.err.println("SQL Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    catch (Exception e) {
      System.err.println("Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
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
    try {
      PreparedStatement statement = db.prepareStatement("""
        delete from selection_recipe where recipe_id = ?
        """);
      statement.setInt(1, recipe.getId());
      statement.executeUpdate();

      statement = db.prepareStatement("""
        delete from admin_week_recipes where recipe_id = ?
        """);
      statement.setInt(1, recipe.getId());
      statement.executeUpdate();

      statement = db.prepareStatement("""
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

      return 1;
    }
    catch (SQLException e) {
      System.err.println("SQL Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    catch (Exception e) {
      System.err.println("Exception " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
