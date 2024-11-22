using api.dto;
using api.services;
using grpc;
using Microsoft.AspNetCore.Mvc;
using Via.Dk;
using LoginResponse = api.Enums.LoginResponse;

namespace api;

[ApiController]
[Route("api/[controller]")]
public class RegistrationController(GrpcClient grpcService) : ControllerBase
{
    [HttpPost]
    public async Task<string> Post([FromBody] CreateRegistrationRequest req)
    {
        string hashed = PasswordService.Hash(req.Password);
        return await grpcService.CreateRegistration(req.Email, hashed, req.IsAdmin);
    }
    
    [HttpPost]
    [Route("login")]
    public async Task<LoginResponse> Login([FromBody] LoginRequest req)
    {
        string hashed = null;
        try
        {
            hashed = await grpcService.Login(req.Email, req.Password);
        }
        catch (Exception e)
        {
            return LoginResponse.UserNotFound;
        }
        bool isCorrect = PasswordService.Verify(req.Password, hashed);
        if (isCorrect)
        {
            return LoginResponse.Success;
        }

        return LoginResponse.WrongPassword;
    }
}
