package via.dk.model.recipe;

import via.dk.model.ingredient.IngredientModel;

import java.sql.Timestamp;
import java.util.List;

public class Recipe
{
  private Integer id;
  private String name;
  private String type;
  private final boolean containsAllergen;
  private int calories;
  private final Timestamp creationDate;
  private final Timestamp modificationDate;
  private final String imageLink;
  private final List<IngredientModel> ingredients;

  public Recipe(int id, String name, String type, boolean containsAllergen,
      int calories, Timestamp creationDate, Timestamp modificationDate, String imageLink, List<IngredientModel> ingredients)
  {
    this.id = id;
    this.name = name;
    this.type = type;
    this.containsAllergen = containsAllergen;
    this.calories = calories;
    this.creationDate = creationDate;
    this.modificationDate = modificationDate;
    this.imageLink = imageLink;
    this.ingredients = ingredients;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getImageLink()
  {
    return imageLink;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public boolean isContainsAllergen()
  {
    return containsAllergen;
  }

  public int getCalories()
  {
    return calories;
  }

  public void setCalories(int calories)
  {
    this.calories = calories;
  }

  public Timestamp getCreationDate()
  {
    return creationDate;
  }

  public Timestamp getModificationDate()
  {
    return modificationDate;
  }

  public List<IngredientModel> getIngredients()
  {
    return ingredients;
  }
}
