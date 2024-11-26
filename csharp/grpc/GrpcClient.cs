﻿using System.Collections;
using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class GrpcClient
{
    private readonly RegistrationService.RegistrationServiceClient _client;

    public RecipesClient RecipesClient { get; }
    public IngredientsClient IngredientsClient { get; }

    public GrpcClient()
    {
        var channel = GrpcChannel.ForAddress("http://localhost:8181");
        this._client = new RegistrationService.RegistrationServiceClient(channel);

        this.RecipesClient = new RecipesClient(channel);
        this.IngredientsClient = new IngredientsClient(channel);
    }

    public async Task<string> CreateRegistration(String email, String password, bool isAdmin)
    {
        var request = new CreateRegistrationRequest()
        {
            Email = email,
            Password = password,
            IsAdmin = isAdmin
        };
        var response = await this._client.CreateRegistrationAsync(request);
        return response.Status;
    }

    public async Task<string> CreateCustomerInformation(String firstName, String lastName, String country, String city,
        String address, String postalCode, String phoneNumber)
    {
        var request = new CreateCustomerInformationRequest()
        {
            FirstName = firstName,
            LastName = lastName,
            Phone = phoneNumber,
            CountryName = country,
            CityName = city,
            StreetName = address,
            PostNumber = postalCode,
        };
        var response = await this._client.CreateCustomerInformationAsync(request);
        return response.Status;
    }


    public async Task<LoginResponse?> Login(String email, String password)
    {
        var request = new LoginRequest()
        {
            Email = email,
            Password = password,
        };
        LoginResponse? response = null;
        try
        {
            response = await this._client.LoginAsync(request);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }

        return response;
    }
}