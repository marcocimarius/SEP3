namespace api.dto;

public class UpdateRecipeDto
{
    public int RecipeId { get; set; }
    public string Name { get; set; }
    public string ImageLink { get; set; }
    public List<int> IngredientsIds { get; set; } 
    public string Description { get; set; }
}