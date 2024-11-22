using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class RecipesClient
{
    private readonly RecipeService.RecipeServiceClient _recipeClient;

    public RecipesClient(GrpcChannel channel)
    {
        this._recipeClient = new RecipeService.RecipeServiceClient(channel);
    }
    
    public async Task<string> CreateRecipe()
    {
        return "";
    }

    public async Task<IEnumerable<Recipe>> GetAllRecipes()
    {
        var response = await this._recipeClient.GetAllRecipesAsync(new RetrieveRecipeRequest());
        return response.Recipes;
    }
}