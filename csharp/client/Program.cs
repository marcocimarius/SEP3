using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using BlazorApp1;
using BlazorApp1.Providers;
using Microsoft.AspNetCore.Components.Authorization;
using DefaultNamespace;

var builder = WebAssemblyHostBuilder.CreateDefault(args);
builder.RootComponents.Add<App>("#app");
builder.RootComponents.Add<HeadOutlet>("head::after");

builder.Services.AddAuthorizationCore();
builder.Services.AddScoped(sp => new HttpClient { BaseAddress = new Uri("http://localhost:5035") });
builder.Services.AddScoped<AuthenticationStateProvider, AuthProvider>();
builder.Services.AddCascadingAuthenticationState();

builder.Services.AddSingleton<RecipeInformationService>();
await builder.Build().RunAsync();