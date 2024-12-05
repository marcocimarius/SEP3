using System.Collections;
using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class GrpcClient
{
    public RegistrationClient RegistrationClient { get; }
    public RecipesClient RecipesClient { get; }
    public IngredientsClient IngredientsClient { get; }
    public AdminWeekSelectionClient AdminWeekSelectionClient { get; }
    public SelectionClient SelectionClient { get; }
    public SubscriptionClient SubscriptionClient { get; }

    public GrpcClient()
    {
        var channel = GrpcChannel.ForAddress("http://localhost:8181");
        
        this.RecipesClient = new RecipesClient(channel);
        this.IngredientsClient = new IngredientsClient(channel);
        this.AdminWeekSelectionClient = new AdminWeekSelectionClient(channel);
        this.SelectionClient = new SelectionClient(channel);
        this.RegistrationClient = new RegistrationClient(channel);
        this.SubscriptionClient = new SubscriptionClient(channel);
    }
}