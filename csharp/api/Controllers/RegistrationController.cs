using api.dto;
using api.services;
using grpc;
using Microsoft.AspNetCore.Mvc;
using Via.Dk;

namespace api;

[ApiController]
[Route("api/[controller]")]
public class RegistrationController : ControllerBase
{
    private readonly GrpcClient _grpcService;

    public RegistrationController(GrpcClient grpcService)
    {
        _grpcService = grpcService;
    }

    [HttpPost]
    public async Task<string> Post([FromBody] CreateRegistrationRequest req)
    {
        string hashed = PasswordService.Hash(req.Password);
        return await _grpcService.CreateRegistration(req.Email, hashed, req.IsAdmin);
    }
}
