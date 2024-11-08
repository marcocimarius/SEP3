using api.dto;
using api.services;
using grpc;
using Microsoft.AspNetCore.Mvc;
using Via.Dk;

namespace api;

[ApiController]
[Route("api/[controller]")]
public class RegistrationController(GrpcClient grpcService) : ControllerBase
{
    // POST api/test
    [HttpPost]
    public async Task<String> Post([FromBody] CreateRegistrationRequest req)
    {
        String hashed = PasswordService.Hash(req.Password);
        return await grpcService.CreateRegistration(req.Email, hashed, req.IsAdmin);
    }
}