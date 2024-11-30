using System.Net.Http.Json;
using System.Security.Claims;
using System.Text.Json;
using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.JSInterop;
using Via.Dk;

namespace BlazorApp1.Providers;

public class AuthProvider : AuthenticationStateProvider
{
    private readonly HttpClient httpClient;
    private ClaimsPrincipal currentClaimsPrincipal;
    private readonly IJSRuntime jsRuntime;

    public AuthProvider(HttpClient httpClient, IJSRuntime jsRuntime)
    {
        this.httpClient = httpClient;
        this.jsRuntime = jsRuntime;
    }


    public async Task<AuthenticationState> Login(string username, string password)
    {
        LoginRequest loginRequest = new LoginRequest
        {
            Email = username,
            Password = password
        };
        HttpResponseMessage response = await httpClient.PostAsJsonAsync(
            "api/Registration/login", loginRequest);
        string content = await response.Content.ReadAsStringAsync();
        if (!response.IsSuccessStatusCode)
        {
            throw new Exception(content);
        }

        LoginResponse? loginResponse = JsonSerializer.Deserialize<LoginResponse>(content, new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        });
        var serializedData = JsonSerializer.Serialize(loginResponse);
        await jsRuntime.InvokeVoidAsync("sessionStorage.setItem", "currentUser", serializedData);

        List<Claim> claims = new List<Claim>
        {
            new Claim("Id", loginResponse.Id.ToString()),
            new Claim("Email", loginResponse.Email),
            new Claim("Role", loginResponse.IsAdmin ? "Admin" : "User")
        };
        ClaimsIdentity identity = new ClaimsIdentity(claims, "apiauth");
        currentClaimsPrincipal = new ClaimsPrincipal(identity);

        NotifyAuthenticationStateChanged(
            Task.FromResult(new AuthenticationState(currentClaimsPrincipal)));
        return new AuthenticationState(currentClaimsPrincipal);
    }

    public override async Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        string userAsJson = "";
        try
        {
            userAsJson = await jsRuntime.InvokeAsync<string>("sessionStorage.getItem", "currentUser");
        }
        catch (InvalidOperationException e)
        {
            return new AuthenticationState(new());
        }

        if (string.IsNullOrEmpty(userAsJson))
        {
            return new AuthenticationState(new());
        }

        LoginResponse loginResponse = JsonSerializer.Deserialize<LoginResponse>(userAsJson)!;
        List<Claim> claims = new List<Claim>()
        {
            new Claim("Id", loginResponse.Id.ToString()),
            new Claim("Email", loginResponse.Email),
            new Claim(ClaimTypes.Role, loginResponse.IsAdmin ? "Admin" : "User")
        };
        ClaimsIdentity identity = new ClaimsIdentity(claims, "apiauth");
        ClaimsPrincipal claimsPrincipal = new ClaimsPrincipal(identity);
        return new AuthenticationState(claimsPrincipal);
    }

    public async Task Logout()
    {
        currentClaimsPrincipal = new();
        await jsRuntime.InvokeVoidAsync("sessionStorage.removeItem", "currentUser");
        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(currentClaimsPrincipal)));
    }
}