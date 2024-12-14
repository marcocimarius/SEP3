using api.dto;
using grpc;
using Microsoft.AspNetCore.Mvc;
using Via.Dk;

namespace api.Controllers;

[ApiController]
[Route("api/[controller]")]
public class SelectionController : ControllerBase
{
    private readonly GrpcClient _grpcService;

    public SelectionController(GrpcClient grpcService)
    {
        _grpcService = grpcService;
    }

    [HttpGet]
    public async Task<IResult> GetSelection([FromQuery] DateTime weekStartDate, [FromQuery] DateTime weekEndDate, [FromQuery] int createdById)
    {
        var request = new RetrieveSelectionRequest
        {
            WeekStartDate = Google.Protobuf.WellKnownTypes.Timestamp.FromDateTime(weekStartDate.ToUniversalTime()),
            WeekEndDate = Google.Protobuf.WellKnownTypes.Timestamp.FromDateTime(weekEndDate.ToUniversalTime()),
            CreatedById = createdById
        };
        List<Recipe> recipes = (await _grpcService.SelectionClient.Retrieve(request)).Recipes.ToList();
        if (recipes.Count == 0)
        {
            return Results.NotFound();
        }
        
        return Results.Ok(recipes);
    }

    [HttpPost]
    public async Task<IResult> CreateSelection([FromBody] CreateSelectionDto dto)
    {
        CreateSelectionRequest request = new CreateSelectionRequest()
        {
            AdminWeekId = dto.AdminWeekId,
            CreatedById = dto.CreatedById,
            AdminWeekRecipesId = { dto.AdminWeekRecipesId }
        };
        if ((await _grpcService.SelectionClient.Create(request)).ToLower().Contains("success"))
        {
            return Results.Ok();
        }

        return Results.Problem();
    }

    [HttpPut]
    public async Task<IResult> UpdateSelection([FromBody] UpdateSelectionDto dto)
    {
        UpdateSelectionRequest request = new UpdateSelectionRequest()
        {
            SelectionId = dto.SelectionId,
            RecipeIds = { dto.AdminWeekRecipesId }
        };

        if ((await _grpcService.SelectionClient.Update(request)).ToLower().Contains("success"))
        {
            return Results.Ok();
        }
        
        return Results.Problem();
    }

    [HttpDelete]
    public async Task<IResult> DeleteSelection([FromBody] DeleteSelectionRequest request)
    {
        if ((await _grpcService.SelectionClient.Delete(request)).ToLower().Contains("success"))
        {
            return Results.Ok();
        }
        
        return Results.Problem();
    }
}