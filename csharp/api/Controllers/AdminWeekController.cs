using api.dto.adminWeek;
using grpc;
using Microsoft.AspNetCore.Mvc;
using Via.Dk;

namespace api.Controllers;

[ApiController]
[Route("api/[controller]")]
public class AdminWeekController : ControllerBase
{
    private readonly GrpcClient _grpcService;

    public AdminWeekController(GrpcClient grpcService)
    {
        _grpcService = grpcService;
    }

    [HttpGet]
    public async Task<IResult> GetAll([FromQuery] DateTime weekStartDate, [FromQuery] DateTime weekEndDate)
    {
        var request = new RetrieveAdminWeekSelectionRequest()
        {
            WeekStartDate = Google.Protobuf.WellKnownTypes.Timestamp.FromDateTime(weekStartDate.ToUniversalTime()),
            WeekEndDate = Google.Protobuf.WellKnownTypes.Timestamp.FromDateTime(weekEndDate.ToUniversalTime()),
        };
        List<AdminWeek> weeks = (await this._grpcService.AdminWeekSelectionClient.GetAdminWeekRecipes(request)).AdminWeeks.ToList();
        if (weeks.Count == 0)
        {
            return Results.NotFound();
        }
        return Results.Ok(weeks);
    }

    [HttpPost]
    public async Task<IResult> Create([FromBody] CreateAdminWeekSelectionDto dto)
    {
        CreateAdminWeekSelectionRequest request = new CreateAdminWeekSelectionRequest()
        {
            CreatedById = dto.CreatedById,
            WeekStartDate = dto.WeekStartDate,
            WeekEndDate =  dto.WeekEndDate,
            RecipeIds = { dto.RecipeIds }
        };
        
        string response = (await this._grpcService.AdminWeekSelectionClient.Create(request));
        if (response.ToLower().Contains("success"))
        {
            return Results.Ok(response);
        }

        return Results.Problem();
    }

    [HttpPut]
    public async Task<IResult> Update([FromBody] UpdateAdminWeekSelectionDto dto)
    {
        UpdateAdminWeekSelectionRequest request = new UpdateAdminWeekSelectionRequest()
        {
            CreatedById = dto.CreatedById,
            AdminWeekId = dto.AdminWeekId,
            WeekEndDate = dto.WeekEndDate,
            WeekStartDate = dto.WeekStartDate,
            RecipeIds = { dto.RecipeIds }
        };
        
        string response = (await this._grpcService.AdminWeekSelectionClient.Update(request));
        if (response.ToLower().Contains("success"))
        {
            return Results.Ok(response);
        }
        
        return Results.Problem();
    }

    [HttpDelete]
    public async Task<IResult> Delete([FromBody] DeleteAdminWeekSelectionRequest request)
    {
        string response = (await this._grpcService.AdminWeekSelectionClient.Delete(request));
        if (response.ToLower().Contains("success"))
        {
            return Results.Ok(response);
        }
        
        return Results.Problem();
    } 
}