package via.dk.dao.selection;

import via.dk.*;
import via.dk.util.DatabaseConnection;
import via.dk.util.TimeConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionDaoImpl implements SelectionDao
{
  private final Connection db = DatabaseConnection.getConnection();

  @Override public int create(CreateSelectionRequest request)
      throws SQLException
  {
    try {
      PreparedStatement statement = db.prepareStatement("""
          INSERT INTO selection (created_by_id, admin_week_id) \s
          VALUES (?, ?)
          returning id
          """);
      statement.setInt(1, request.getCreatedById());
      statement.setInt(2, request.getAdminWeekId());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        int newSelectionId = resultSet.getInt("id");
        statement = db.prepareStatement("""
            INSERT INTO selection_recipe (recipe_id, selection_id) \s
            VALUES (?, ?)
            """);
        for (int i = 0; i < request.getAdminWeekRecipesIdList().size(); i++)
        {
          statement.setInt(1, request.getAdminWeekRecipesIdList().get(i));
          statement.setInt(2, newSelectionId);
          statement.addBatch();
        }

        int[] results = statement.executeBatch();
        for (int result : results) {
          if (result <= 0) {
            return -1;
          }
        }
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

    return 1;
  }

  @Override public int update(UpdateSelectionRequest request)
      throws SQLException
  {
    try {
      PreparedStatement statement = db.prepareStatement("""
          delete from selection_recipe
          where selection_id = ?
          """);
      statement.setInt(1, request.getSelectionId());

      if (statement.executeUpdate() != 0) {
        statement = db.prepareStatement("""
            INSERT INTO selection_recipe (recipe_id, selection_id) \s
            VALUES (?, ?)
            """);

        for (int i = 0; i < request.getRecipeIdsList().size(); i++)
        {
          statement.setInt(1, request.getRecipeIdsList().get(i));
          statement.setInt(2, request.getSelectionId());
          statement.addBatch();
        }

        int[] results = statement.executeBatch();
        for (int result : results) {
          if (result <= 0) {
            return -1;
          }
        }
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

    return 1;
  }

  @Override public int delete(DeleteSelectionRequest request)
      throws SQLException
  {
    try
    {
      PreparedStatement statement = db.prepareStatement("""
          delete from selection_recipe where selection_id = ?
          """);
      statement.setInt(1, request.getSelectionId());
      statement.executeUpdate();

      statement = db.prepareStatement("""
          delete from selection where id = ?
          """);
      statement.setInt(1, request.getSelectionId());

      if (statement.executeUpdate() != 0) {
        return 1;
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

    return 0;
  }

  @Override public List<Recipe> retrieve(RetrieveSelectionRequest request)
      throws SQLException
  {
    List<Recipe> recipes = new ArrayList<>();
    List<Ingredient> ingredients = new ArrayList<>();

    PreparedStatement statement = db.prepareStatement("""
        select * from selection
        inner join admin_week on selection.admin_week_id = admin_week.id
        where admin_week.week_start_date = ? \s
              and week_end_date = ? \s
              and selection.created_by_id = ?
        """);

    try
    {
      statement.setTimestamp(1, TimeConverter.toJavaTimestamp(request.getWeekStartDate()));
      statement.setTimestamp(2, TimeConverter.toJavaTimestamp(request.getWeekEndDate()));
      statement.setInt(3, request.getCreatedById());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        int selectionId = resultSet.getInt("id");

        statement = db.prepareStatement("""
          select * from recipe
          inner join selection_recipe on recipe.id = selection_recipe.recipe_id
          where selection_id = ?
          """);
        statement.setInt(1, selectionId);
        resultSet = statement.executeQuery();

        while (resultSet.next()) {
          int recipeId = resultSet.getInt("id");
          String name = resultSet.getString("name");
          String type = resultSet.getString("type");
          boolean containsAllergen = resultSet.getBoolean("contains_allergen");
          int calories = resultSet.getInt("calories");
          Timestamp creationDate = resultSet.getTimestamp("creation_date");
          Timestamp modificationDate = resultSet.getTimestamp("modification_date");
          String imageLink = resultSet.getString("image_link");
          String description = resultSet.getString("description");

          PreparedStatement ingredientsStatement = db.prepareStatement("""
            select ingredient.*, t.type from ingredient
            inner join recipes_with_ingredients rwi on ingredient.id = rwi.ingredient_id
            inner join ingredient_type it on ingredient.id = it.ingredient_id
            inner join type t on it.type_id = t.id
            where recipe_id = ?
            group by ingredient.id, t.type
            order by id;
            """);
          ingredientsStatement.setInt(1, recipeId);
          ResultSet ingredientsResultSet = ingredientsStatement.executeQuery();

          while (ingredientsResultSet.next()) {
            int id = ingredientsResultSet.getInt("id");
            String ingredientName = ingredientsResultSet.getString("name");
            int ingredientCalories = ingredientsResultSet.getInt("calories");
            boolean isIngredientAllergen = ingredientsResultSet.getBoolean("is_allergen");
            Timestamp ingredientCreationDate = ingredientsResultSet.getTimestamp("creation_date");
            Timestamp ingredientModificationDate = ingredientsResultSet.getTimestamp("modification_date");
            String ingredientType = ingredientsResultSet.getString("type");
            Ingredient.Builder ingredient = Ingredient.newBuilder()
                .setId(id)
                .setName(ingredientName)
                .setCalories(ingredientCalories)
                .setIsAllergen(isIngredientAllergen)
                .setCreationDate(TimeConverter.toProtobufTimestamp(ingredientCreationDate))
                .setType(ingredientType);
            if (TimeConverter.toProtobufTimestamp(ingredientModificationDate) != null) {
              ingredient.setModificationDate(TimeConverter.toProtobufTimestamp(ingredientModificationDate));
            }
            ingredient.build();
            ingredients.add(ingredient.build());
          }

          Recipe.Builder recipe = Recipe.newBuilder()
              .setId(recipeId)
              .setName(name)
              .setType(type)
              .setContainsAllergen(containsAllergen)
              .setCalories(calories)
              .setCreationDate(TimeConverter.toProtobufTimestamp(creationDate))
              .setImageLink(imageLink == null ? " " : imageLink)
              .setDescription(description)
              .addAllIngredients(ingredients);

          if (modificationDate != null) {
            recipe.setModificationDate(TimeConverter.toProtobufTimestamp(modificationDate));
          }
          recipe.build();
          recipes.add(recipe.build());
        }
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
}
