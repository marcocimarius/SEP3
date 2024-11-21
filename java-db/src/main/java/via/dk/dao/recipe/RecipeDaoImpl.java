package via.dk.dao.recipe;

import via.dk.model.recipe.Recipe;
import via.dk.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDaoImpl implements IRecipeDao
{
  private final Connection db = DatabaseConnection.getConnection();

  @Override public int create(Recipe recipe) throws SQLException
  {
    return 0;
  }

  @Override public List<Recipe> getAllRecipes() throws SQLException
  {
    PreparedStatement statement = db.prepareStatement("select * from recipe");
    ResultSet resultSet = statement.executeQuery();
    List<Recipe> recipes = new ArrayList<>();
    while (resultSet.next()) {
      recipes.add(new Recipe(resultSet.getInt("id"), resultSet.getString("name"),
          resultSet.getString("type"), resultSet.getBoolean("contains_allergen"), resultSet.getInt("calories"),
          resultSet.getTimestamp("creation_date"), resultSet.getTimestamp("modification_date"), resultSet.getString("image_link")));
    }
    return recipes;
  }
}
