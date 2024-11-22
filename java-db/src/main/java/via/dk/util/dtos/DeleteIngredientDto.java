package via.dk.util.dtos;

public class DeleteIngredientDto
{
  private int id;

  public DeleteIngredientDto(int id)
  {
    this.id = id;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }
}
