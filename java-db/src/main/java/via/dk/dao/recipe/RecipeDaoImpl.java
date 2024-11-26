package via.dk.dao.recipe;

import via.dk.CreateRecipeRequest;
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
        """);
    statement.setString(1, recipe.getName());
    statement.setString(2, recipe.getImageLink());
    if (statement.executeUpdate() != 0) {
      statement = db.prepareStatement("""
          insert into ingredient_type (type_id, ingredient_id)
          values (?, ?)
          """);
      for (int i = 0; i < recipe.getIngredientsIdList().size(); i++)
      {
        PreparedStatement typeIdStatement = db.prepareStatement("""
            select type.id from type
            inner join ingredient_type on type.id = ingredient_type.type_id
            where ingredient_type.ingredient_id = ?""");
        typeIdStatement.setInt(1, recipe.getIngredientsIdList().get(i));
        ResultSet typeIdResultSet = typeIdStatement.executeQuery();
        int type_id = typeIdResultSet.getInt("id");
        statement.setInt(1, type_id);
        statement.setInt(2, recipe.getIngredientsIdList().get(i));
        statement.addBatch();
      }

      int[] results = statement.executeBatch();
      for (int result : results) {
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

    while (resultSet.next()) {
      if (!resultSet.wasNull()) {
        int recipeId = resultSet.getInt("id");
        PreparedStatement recipeStatement = db.prepareStatement("""
          select ingredient.*, type.type from ingredient
          inner join recipes_with_ingredients on ingredient.id = recipes_with_ingredients.ingredient_id
          inner join ingredient_type on ingredient.id = ingredient_type.ingredient_id
          inner join type on ingredient_type.type_id = type.id
          where recipes_with_ingredients.recipe_id = ?
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
}
