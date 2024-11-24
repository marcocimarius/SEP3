using api.Responses;
using api.services;
using grpc;
namespace api.Controllers;
using Microsoft.AspNetCore.Mvc;
using Via.Dk;


[ApiController]
[Route("api/[controller]")]
public class RegistrationController(GrpcClient grpcService) : ControllerBase
{
    [HttpPost]
    public async Task<ActionResult<string>> Post([FromBody] CreateRegistrationRequest req)
    {
        string hashed = PasswordService.Hash(req.Password);
        return await grpcService.CreateRegistration(req.Email, hashed, req.IsAdmin);
    }
    
    [HttpPost]
    [Route("login")]
    public async Task<ActionResult<ApiLoginResponse>> Login([FromBody] LoginRequest req)
    {
        LoginResponse? res = await grpcService.Login(req.Email, req.Password);
        if (res == null) return NotFound();
        ApiLoginResponse apiRes = new ApiLoginResponse
        {
            Id = res.Id,
            IsAdmin = res.IsAdmin,
            Email = res.Email
        };
        bool logged = PasswordService.Verify(req.Password, res.Password);
        if (!logged) return Unauthorized();
        return apiRes;
    }
}
