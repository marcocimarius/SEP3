package via.dk.model.recipe;

public class Recipe
{
  private Integer id;
  private String name;
  private String type;
  private boolean containsAllergen;
  private int calories;
  private String creationDate;
  private String modificationDate;
  private String imageLink;

  public Recipe(int id, String name, String type, boolean containsAllergen,
      int calories, String creationDate, String modificationDate, String imageLink)
  {
    this.id = id;
    this.name = name;
    this.type = type;
    this.containsAllergen = containsAllergen;
    this.calories = calories;
    this.creationDate = creationDate;
    this.modificationDate = modificationDate;
    this.imageLink = imageLink;
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

  public String getCreationDate()
  {
    return creationDate;
  }

  public void setCreationDate(String creationDate)
  {
    this.creationDate = creationDate;
  }

  public String getModificationDate()
  {
    return modificationDate;
  }

  public void setModificationDate(String modificationDate)
  {
    this.modificationDate = modificationDate;
  }
}
