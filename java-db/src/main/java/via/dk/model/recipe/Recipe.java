package via.dk.model.recipe;

import via.dk.Ingredient;
import via.dk.model.ingredient.IngredientModel;

import java.sql.Timestamp;
import java.util.List;

public class Recipe
{
  private Integer id;
  private String name;
  private String type;
  private boolean containsAllergen;
  private int calories;
  private Timestamp creationDate;
  private Timestamp modificationDate;
  private String imageLink;
  private List<IngredientModel> ingredients;

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

  public void setImageLink(String imageLink)
  {
    this.imageLink = imageLink;
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

  public void setContainsAllergen(boolean containsAllergen)
  {
    this.containsAllergen = containsAllergen;
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

  public void setCreationDate(Timestamp creationDate)
  {
    this.creationDate = creationDate;
  }

  public Timestamp getModificationDate()
  {
    return modificationDate;
  }

  public void setModificationDate(Timestamp modificationDate)
  {
    this.modificationDate = modificationDate;
  }

  public List<IngredientModel> getIngredients()
  {
    return ingredients;
  }

  @Override public String toString()
  {
    return "Recipe{" + "id=" + id + ", name='" + name + '\'' + ", type='" + type
        + '\'' + ", containsAllergen=" + containsAllergen + ", calories="
        + calories + ", creationDate=" + creationDate + ", modificationDate="
        + modificationDate + ", imageLink='" + imageLink + '\''
        + ", ingredients=" + ingredients + '}';
  }
}
