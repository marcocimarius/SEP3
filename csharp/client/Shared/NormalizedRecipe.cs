using Via.Dk;

namespace DefaultNamespace;

public class NormalizedRecipe
{
    public int Id { get; set; }
    public string? Name { get; set; }
    public string? Description { get; set; }
    public string? ImageLink { get; set; }
    public string? Type { get; set; }
    public List<Ingredient>? Ingredients { get; set; } = new ();
}