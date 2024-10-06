using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class GrpcClient
{
    private readonly TestService.TestServiceClient _client;
    public GrpcClient()
    {
        var channel = GrpcChannel.ForAddress("http://localhost:8181");
        this._client = new TestService.TestServiceClient(channel);
    }
    public async Task<String> AddTest(TestRequest req)
    {
        var reply = await this._client.helloAsync(req);
        return reply.Greeting; 
    }
}