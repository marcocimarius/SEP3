namespace api.dto;

public class CreateRecipeDto
{
    public string Name { get; set; }
    public string ImageLink { get; set; }
    public List<int> IngredientsIds { get; set; }
}