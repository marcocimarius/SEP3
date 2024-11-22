using Microsoft.AspNetCore.Components.Authorization;

namespace BlazorApp1.Providers;

public class AuthProvider:AuthenticationStateProvider
{
    public override Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        throw new NotImplementedException()
    }
}