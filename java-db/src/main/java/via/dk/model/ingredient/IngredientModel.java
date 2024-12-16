package via.dk.model.ingredient;

import java.sql.Timestamp;

/**
 * Model class for the IngredientModel object.
 */
public class IngredientModel
{
  private Integer id;
  private String name;
  private int calories;
  private boolean isAllergen;
  private Timestamp creationDate;
  private Timestamp modificationDate;
  private String type;

	/**
	 * Constructor for the IngredientModel class.
	 * @param id The id of the ingredient.
	 * @param name The name of the ingredient.
	 * @param calories 		The calories of the ingredient.
	 * @param isAllergen 	Whether the ingredient is an allergen or not.
	 * @param creationDate 	The creation date of the ingredient.
	 * @param modificationDate 	The modification date of the ingredient.
	 * @param type  The type of the ingredient.
	 */
  public IngredientModel(Integer id, String name, int calories, boolean isAllergen,
      Timestamp creationDate, Timestamp modificationDate, String type)
  {
    this.id = id;
    this.name = name;
    this.calories = calories;
    this.isAllergen = isAllergen;
    this.creationDate = creationDate;
    this.modificationDate = modificationDate;
    this.type = type;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public int getCalories()
  {
    return calories;
  }

  public void setCalories(int calories)
  {
    this.calories = calories;
  }

  public boolean isAllergen()
  {
    return isAllergen;
  }

  public void setAllergen(boolean allergen)
  {
    isAllergen = allergen;
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

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }
}
