using grpc;
using Microsoft.AspNetCore.Mvc;
using Via.Dk;

namespace api.Controllers;

[ApiController]
[Route("[controller]")]
public class SubscriptionController : ControllerBase
{
    private readonly GrpcClient _grpcService;

    public SubscriptionController(GrpcClient grpcService)
    {
        _grpcService = grpcService;
    }

    [HttpGet("{registration_id}:int")]
    public async Task<IResult> GetSubscription([FromRoute] int registration_id)
    {
        RetrieveSubscriptionRequest request = new RetrieveSubscriptionRequest()
        {
            RegistrationId = registration_id
        };
        RetrieveSubscriptionResponse response = (await _grpcService.SubscriptionClient.Retrieve(request));
        if (response.Id != 0)
        {
            return Results.Ok(response);
        }

        return Results.NotFound();
    }

    [HttpPost("{registrationId}:int")]
    public async Task<IResult> CreateSubscription([FromRoute] int registrationId)
    {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest()
        {
            RegistrationId = registrationId
        };

        string status = (await _grpcService.SubscriptionClient.Create(request));
        if (status.ToLower().Contains("success"))
        {
            return Results.Ok();
        }

        return Results.Problem();
    }

    [HttpPut("{subscriptionId}:int")]
    public async Task<IResult> CancelSubscription([FromRoute] int subscriptionId)
    {
        CancelSubscriptionRequest request = new CancelSubscriptionRequest()
        {
            Id = subscriptionId
        };
        
        string status = (await _grpcService.SubscriptionClient.Cancel(request));
        if (status.ToLower().Contains("success"))
        {
            return Results.Ok();
        }

        return Results.Problem();
    }
}