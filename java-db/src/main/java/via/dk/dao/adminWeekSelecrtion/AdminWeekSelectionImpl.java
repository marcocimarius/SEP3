package via.dk.dao.adminWeekSelecrtion;

import via.dk.*;
import via.dk.util.DatabaseConnection;
import via.dk.util.TimeConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminWeekSelectionImpl implements AdminWeekSelectionDao
{
  private final Connection db = DatabaseConnection.getConnection();

  @Override public int createAdminWeekSelection(
      CreateAdminWeekSelectionRequest request) throws SQLException
  {
    try
    {
      PreparedStatement statement = db.prepareStatement("""
        insert into admin_week (created_by_id, week_start_date, week_end_date) \s
        values (?, ?, ?)
        returning id
        """);
      statement.setInt(1, request.getCreatedById());
      statement.setTimestamp(2, TimeConverter.toJavaTimestamp(request.getWeekStartDate()));
      statement.setTimestamp(3, TimeConverter.toJavaTimestamp(request.getWeekEndDate()));
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        int newAdminWeekId = resultSet.getInt("id");
        statement = db.prepareStatement("""
          insert into admin_week_recipes (recipe_id, admin_week_id) \s
          values (?, ?)
          """);
        for (int i = 0; i < request.getRecipeIdsList().size(); i++)
        {
          statement.setInt(1, request.getRecipeIdsList().get(i));
          statement.setInt(2, newAdminWeekId);
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

  @Override public int deleteAdminWeekSelection(
      DeleteAdminWeekSelectionRequest request) throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("""
        delete from admin_week_recipes
        where admin_week_id = ?
        """);
    statement.setInt(1, request.getAdminWeekId());
    statement.executeUpdate();

    statement = db.prepareStatement("""
        delete from admin_week
        where id = ?
        """);
    statement.setInt(1, request.getAdminWeekId());
    if (statement.executeUpdate() != 0) {
      return 1;
    }

    return 0;
  }

  @Override public int updateAdminWeekSelection(
      UpdateAdminWeekSelectionRequest request) throws SQLException
  {
    try {
      PreparedStatement statement = db.prepareStatement("""
        update admin_week \s
        set created_by_id = ?,
            week_start_date = ?,
            week_end_date = ?,
            modification_date = ?
        where id = ?
        """);

      statement.setInt(1, request.getCreatedById());
      statement.setTimestamp(2, TimeConverter.toJavaTimestamp(request.getWeekStartDate()));
      statement.setTimestamp(3, TimeConverter.toJavaTimestamp(request.getWeekEndDate()));
      statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      statement.setInt(5, request.getAdminWeekId());

      if (statement.executeUpdate() != 0) {
        statement = db.prepareStatement("""
          delete from admin_week_recipes \s
          where admin_week_id = ?
          """);
        statement.setInt(1, request.getAdminWeekId());

        if (statement.executeUpdate() != 0) {
          statement = db.prepareStatement("""
              insert into admin_week_recipes (recipe_id, admin_week_id) \s
              values (?, ?)
              """);

          for (int i = 0; i < request.getRecipeIdsList().size(); i++)
          {
            statement.setInt(1, request.getRecipeIdsList().get(i));
            statement.setInt(2, request.getAdminWeekId());
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

  @Override public List<AdminWeek> retrieveAdminWeekSelection(RetrieveAdminWeekSelectionRequest request) throws SQLException
  {
    List<AdminWeek> adminWeekSelections = new ArrayList<>();
    List<Recipe> recipes = new ArrayList<>();
    List<Ingredient> ingredients = new ArrayList<>();

    PreparedStatement statement = db.prepareStatement("""
        select * from admin_week \s
        where week_start_date = ? and week_end_date = ?
        """);
    statement.setTimestamp(1, TimeConverter.toJavaTimestamp(request.getWeekStartDate()));
    statement.setTimestamp(2, TimeConverter.toJavaTimestamp(request.getWeekEndDate()));
    ResultSet resultSet = statement.executeQuery();

    try {
      if (resultSet.next()) {
        int adminWeekId = resultSet.getInt("id");
        Timestamp weekStartDate = resultSet.getTimestamp("week_start_date");
        Timestamp weekEndDate = resultSet.getTimestamp("week_end_date");

        PreparedStatement recipeStatement = db.prepareStatement("""
            select recipe.* from recipe
            inner join admin_week_recipes awr on recipe.id = awr.recipe_id
            where awr.admin_week_id = ?
          """);
        recipeStatement.setInt(1, adminWeekId);
        ResultSet recipesResultSet = recipeStatement.executeQuery();
        while (recipesResultSet.next()) {
          int recipeId = recipesResultSet.getInt("id");
          String name = recipesResultSet.getString("name");
          String type = recipesResultSet.getString("type");
          boolean containsAllergen = recipesResultSet.getBoolean("contains_allergen");
          int calories = recipesResultSet.getInt("calories");
          Timestamp creationDate = recipesResultSet.getTimestamp("creation_date");
          Timestamp modificationDate = recipesResultSet.getTimestamp("modification_date");
          String imageLink = recipesResultSet.getString("image_link");

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
              .addAllIngredients(ingredients);

          if (modificationDate != null) {
            recipe.setModificationDate(TimeConverter.toProtobufTimestamp(modificationDate));
          }
          recipe.build();
          recipes.add(recipe.build());
        }

        AdminWeek.Builder adminWeek = AdminWeek.newBuilder()
            .setId(adminWeekId)
            .setWeekStartDate(TimeConverter.toProtobufTimestamp(weekStartDate))
            .setWeekEndDate(TimeConverter.toProtobufTimestamp(weekEndDate))
            .addAllRecipes(recipes);

        adminWeekSelections.add(adminWeek.build());
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

    return adminWeekSelections;
  }
}
