namespace api.dto;

public class CreateSelectionDto
{
    public int CreatedById { get; set; }
    public int AdminWeekId { get; set; }
    public List<int> AdminWeekRecipesId { get; set; }
}