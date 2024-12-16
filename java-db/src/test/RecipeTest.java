import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import via.dk.CreateRecipeRequest;
import via.dk.dao.ingredients.IngredientsDaoImpl;
import via.dk.dao.recipe.RecipeDaoImpl;
import via.dk.model.ingredient.IngredientModel;
import via.dk.util.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class RecipeTest {
	static Connection con;
	static IngredientsDaoImpl ingredientDao;
	static RecipeDaoImpl recipeDao;

	@BeforeAll
	public static void setUp() {
		con = DatabaseConnection.getConnection();
		ingredientDao = new IngredientsDaoImpl();
		recipeDao = new RecipeDaoImpl();
	}

	private void createIngredients() {
		// Arrange
		IngredientModel test1 = new IngredientModel(1, "Test1", 10, false, null, null, "Vegetable");
		IngredientModel test2 = new IngredientModel(2, "Test2", 100, false, null, null, "Grain");
		// Act
		try {
			ingredientDao.createIngredient(test1, 1);
			ingredientDao.createIngredient(test2, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void createRecipe() {
		// Arrange
		createIngredients();
		// Act
		try {
			List<IngredientModel> ingrs = ingredientDao.getAllIngredients();
			// get the ids of the ingredients of name Test1 and Test2
			List<Integer> ingIds = List.of(ingrs.stream().filter(i -> i.getName().equals("Test1")).findFirst().get().getId(),
					ingrs.stream().filter(i -> i.getName().equals("Test2")).findFirst().get().getId());
			CreateRecipeRequest recipe = CreateRecipeRequest.newBuilder()
					.setName("Test recipe")
					.addAllIngredientsId(ingIds)
					.setDescription("Boil pasta and add tomato")
					.setImageLink("https://www.google.com")
					.build();
			int status = recipeDao.create(recipe);
			// find the created recipe by name
			var createdRecipe = recipeDao.getAllRecipes().stream().filter(r -> r.getName().equals("Test recipe")).findFirst().orElse(null);
			// Assert that the calories are calculated correctly, the type is 2, allergen is false, and the recipe is created
			Assertions.assertEquals(110, createdRecipe.getCalories());
			Assertions.assertEquals("vegetarian", createdRecipe.getType());
			Assertions.assertFalse(createdRecipe.getContainsAllergen());
			Assertions.assertEquals(1, status);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@AfterAll
	public static void tearDown() {
		try {
//			org.postgresql.util.PSQLException: ERROR: update or delete on table "recipe" violates foreign key constraint "recipes_with_ingredients_recipe_id_fkey" on table "recipes_with_ingredients"
//			Detail: Key (id)=(8) is still referenced from table "recipes_with_ingredients".
			var recipe = recipeDao.getAllRecipes().stream().filter(r -> r.getName().equals("Test recipe")).findFirst().orElse(null);
			List<IngredientModel> ingrs = ingredientDao.getAllIngredients();
			List<Integer> ingIds = List.of(ingrs.stream().filter(i -> i.getName().equals("Test1")).findFirst().get().getId(),
					ingrs.stream().filter(i -> i.getName().equals("Test2")).findFirst().get().getId());
			// delete the ingredients from mapping table recipe_with_ingredients
			con.prepareStatement("delete from ingredient_type where ingredient_id = " + ingIds.get(0)).executeUpdate();
			con.prepareStatement("delete from ingredient_type where ingredient_id = " + ingIds.get(1)).executeUpdate();
			con.prepareStatement("delete from recipes_with_ingredients where recipe_id = " + recipe.getId()).executeUpdate();
			con.prepareStatement("delete from recipe where name = 'Test recipe'").executeUpdate();
			con.prepareStatement("delete from ingredient where name = 'Test1'").executeUpdate();
			con.prepareStatement("delete from ingredient where name = 'Test2'").executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
