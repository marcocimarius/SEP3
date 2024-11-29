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
    
    public async Task<string> CreateRecipe(CreateRecipeRequest createRecipeRequest)
    {
        return (await this._recipeClient.CreateRecipeAsync(createRecipeRequest)).Status;
    }
    
    public async Task<string> UpdateRecipe(UpdateRecipeRequest updateRecipeRequest)
    {
        return (await this._recipeClient.UpdateRecipeAsync(updateRecipeRequest)).Status;
    }
    
    public async Task<string> DeleteRecipe(DeleteRecipeRequest deleteRecipeRequest)
    {
        return (await this._recipeClient.DeleteRecipeAsync(deleteRecipeRequest)).Status;
    }
    
    public async Task<IEnumerable<Recipe>> GetAllRecipes()
    {
        return (await this._recipeClient.GetAllRecipesAsync(new RetrieveRecipeRequest())).Recipes;
    }
}