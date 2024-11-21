using System.Collections;
using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class GrpcClient
{
    private readonly RegistrationService.RegistrationServiceClient _client;
    
    public RecipesClient RecipesClient { get; }
    public IngredientsClient IngredientsClient { get; }
    
    public GrpcClient()
    {
        var channel = GrpcChannel.ForAddress("http://localhost:8181");
        this._client = new RegistrationService.RegistrationServiceClient(channel);
        
        this.RecipesClient = new RecipesClient(channel);
        this.IngredientsClient = new IngredientsClient(channel);
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
}