namespace api.dto;

public class UpdateRecipeDto
{
    public int Id { get; set; }
    public string Name { get; set; }
    public string ImageLink { get; set; }
    public List<int> IngredientsId { get; set; } 
    public string Description { get; set; }
}