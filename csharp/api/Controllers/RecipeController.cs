﻿using api.dto;
using grpc;
using Grpc.Core;
using Microsoft.AspNetCore.Mvc;
using Via.Dk;

namespace api;

[ApiController]
[Route("api/[controller]")]
public class RecipeController : ControllerBase
{
    private readonly GrpcClient _grpcService;

    public RecipeController(GrpcClient grpcService)
    {
        _grpcService = grpcService;
    }

    [HttpGet("allRecipes")]
    //the Recipe class is auto generated by the proto file and idk if this can cause problems in the future
    public async Task<IResult> GetRecipes()
    {
        List<Recipe> recipes = (await _grpcService.RecipesClient.GetAllRecipes()).ToList();
        if (recipes.Count == 0)
        {
            return Results.NotFound("No recipes exist");
        }
        
        return Results.Ok(recipes);
    }

    [HttpPost]
    public async Task<IResult> AddRecipe([FromBody] CreateRecipeDto recipe)
    {
        CreateRecipeRequest request = new CreateRecipeRequest()
        {
            Name = recipe.Name,
            ImageLink = recipe.ImageLink,
            IngredientsId = { recipe.IngredientsId },
            Description = recipe.Description
        };
        Console.WriteLine(request);
        try
        {
            string response = await _grpcService.RecipesClient.CreateRecipe(request);
            return response.ToLower().Contains("success") ? Results.Ok(response) : Results.Conflict(response);
        }
        catch (RpcException ex)
        {
            Console.WriteLine($"gRPC Error: {ex.Status.Detail}");
            return Results.Problem($"gRPC Error: {ex.Status}");
        }
    }

    [HttpPut]
    public async Task<IResult> UpdateRecipe([FromBody] UpdateRecipeDto recipe)
    {
        UpdateRecipeRequest recipeRequest = new UpdateRecipeRequest()
        {
            Id = recipe.Id,
            Name = recipe.Name,
            ImageLink = recipe.ImageLink,
            IngredientsId = { recipe.IngredientsId },
            Description = recipe.Description
        };
        string response = await _grpcService.RecipesClient.UpdateRecipe(recipeRequest);
        if (response.ToLower().Contains("success"))
        {
            return Results.Ok(response);
        }
        
        return Results.Conflict(response);
    }

    [HttpDelete]
    public async Task<IResult> DeleteRecipe([FromBody] DeleteRecipeRequest recipe)
    {
        string response = await _grpcService.RecipesClient.DeleteRecipe(recipe);
        if (response.ToLower().Contains("success"))
        {
            return Results.Ok(response);
        }

        return Results.Conflict(response);
    }
}