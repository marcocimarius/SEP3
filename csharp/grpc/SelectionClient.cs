using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class SelectionClient
{
    private readonly SelectionService.SelectionServiceClient _client;

    public SelectionClient(GrpcChannel channel)
    {
        _client = new SelectionService.SelectionServiceClient(channel);
    }

    public async Task<string> Create(CreateSelectionRequest request)
    {
        return (await _client.CreateSelectionAsync(request)).Status;
    }

    public async Task<string> Delete(DeleteSelectionRequest request)
    {
        return (await _client.DeleteSelectionAsync(request)).Status;
    }

    public async Task<string> Update(UpdateSelectionRequest request)
    {
        return (await _client.UpdateSelectionAsync(request)).Status;
    }

    public async Task<RetrieveSelectionResponse> Retrieve(RetrieveSelectionRequest request)
    {
        return await _client.RetrieveSelectionAsync(request);
    }
}