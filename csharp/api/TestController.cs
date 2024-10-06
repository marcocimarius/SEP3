using api.dto;
using grpc;
using Microsoft.AspNetCore.Mvc;
using Via.Dk;

namespace api;

[ApiController]
[Route("api/[controller]")]
public class TestController(GrpcClient grpcService) : ControllerBase
{
    // POST api/test
    [HttpPost]
    public async Task<String> Post([FromBody] TestRequest req)
    {
        return await grpcService.AddTest(req);
    }
}