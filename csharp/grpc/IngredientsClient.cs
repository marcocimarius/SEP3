using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class IngredientsClient
{
    private readonly IngredientsService.IngredientsServiceClient _ingredientsClient;

    public IngredientsClient(GrpcChannel channel)
    {
        this._ingredientsClient = new IngredientsService.IngredientsServiceClient(channel);
    }

    public async Task<string> CreateIngredientAsync(CreateIngredientRequest request)
    {
        return (await this._ingredientsClient.CreateIngredientAsync(request)).Status;
    }

    public async Task<string> DeleteIngredientAsync(DeleteIngredientRequest request)
    {
        return (await this._ingredientsClient.DeleteIngredientAsync(request)).Status;
    }

    public async Task<string> UpdateIngredientAsync(UpdateIngredientRequest ingredient)
    {
        return (await this._ingredientsClient.UpdateIngredientAsync(ingredient)).Status;
    }

    public async Task<IEnumerable<Ingredient>> GetAllIngredientsAsync()
    {
        return (await this._ingredientsClient.GetAllIngredientsAsync(new GetAllIngredientsRequest())).Ingredients;
    }
}