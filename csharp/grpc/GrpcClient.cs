using Grpc.Net.Client;
using Via.Dk;
using LoginResponse = api.Enums.LoginResponse;

namespace grpc;

public class GrpcClient
{
    private readonly RegistrationService.RegistrationServiceClient _client;
    private readonly RecipeService.RecipeServiceClient _recipeClient;

    public GrpcClient()
    {
        var channel = GrpcChannel.ForAddress("http://localhost:8181");
        this._client = new RegistrationService.RegistrationServiceClient(channel);
        this._recipeClient = new RecipeService.RecipeServiceClient(channel);
    }

    public async Task<string> CreateRegistration(String email, String password, bool isAdmin)
    {
        var request = new CreateRegistrationRequest()
        {
            Email = email,
            Password = password,
            IsAdmin = isAdmin
        };
        var response = await this._client.CreateRegistrationAsync(request);
        return response.Status;
    }

    public async Task<string> Login(String email, String password)
    {
        var request = new LoginRequest()
        {
            Email = email,
            Password = password,
        };
        var response = await this._client.LoginAsync(request);
        return response.HashedPassword;
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