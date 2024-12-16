package via.dk.util.dtos;

/**
 * Data transfer object for deleting an ingredient.
 */
public class DeleteIngredientDto
{
  private int id;

	/**
	 * Constructor for the DeleteIngredientDto class.
	 * @param id
	 */
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
