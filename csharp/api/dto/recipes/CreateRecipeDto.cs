namespace api.dto;

public class CreateRecipeDto
{
    public string Name { get; set; }
    public string ImageLink { get; set; }
    public List<int> IngredientsId { get; set; }
    public string Description { get; set; }
}