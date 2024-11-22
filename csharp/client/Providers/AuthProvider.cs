using System.Net.Http.Json;
using System.Security.Claims;
using System.Text.Json;
using Microsoft.AspNetCore.Components.Authorization;
using Via.Dk;

namespace BlazorApp1.Providers;

public class AuthProvider : AuthenticationStateProvider
{
    private readonly HttpClient httpClient;
    private ClaimsPrincipal currentClaimsPrincipal;
    
    public AuthProvider(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }

    
    public async Task Login(string username, string password)
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
    }

    public override async Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        return new AuthenticationState(currentClaimsPrincipal ?? new());
    }

    public async Task Logout()
    {
        currentClaimsPrincipal = new();
        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(currentClaimsPrincipal)));
    }
}