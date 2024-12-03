using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class SubscriptionClient
{
    private readonly SubscriptionService.SubscriptionServiceClient _subscriptionServiceClient;

    public SubscriptionClient(GrpcChannel channel)
    {
        _subscriptionServiceClient = new SubscriptionService.SubscriptionServiceClient(channel);
    }

    public async Task<string> Create(CreateSubscriptionRequest request)
    {
        return (await _subscriptionServiceClient.CreateSubscriptionAsync(request)).Status;
    }
    
    public async Task<string> Cancel(CancelSubscriptionRequest request)
    {
        return (await _subscriptionServiceClient.CancelSubscriptionAsync(request)).Status;
    }

    public async Task<RetrieveSubscriptionResponse> Retrieve(RetrieveSubscriptionRequest request)
    {
        return (await _subscriptionServiceClient.RetrieveSubscriptionAsync(request));
    }
}