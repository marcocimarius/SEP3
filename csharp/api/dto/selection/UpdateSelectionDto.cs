namespace api.dto;

public class UpdateSelectionDto
{
    public int SelectionId { get; set; }
    public List<int> AdminWeekRecipesIds { get; set; }
}