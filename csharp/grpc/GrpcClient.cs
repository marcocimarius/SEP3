using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class GrpcClient
{
    private readonly TestService.TestServiceClient _client;
    public GrpcClient()
    {
        using var channel = GrpcChannel.ForAddress("http://localhost:8181");
        this._client = new TestService.TestServiceClient(channel);
    }
    public async Task<String> AddTest()
    {
        var request = new TestRequest{Name = "Dimitar", Description = "Is clever af"};
        var reply = await this._client.helloAsync(request);
        return reply.Greeting; 
    }
}