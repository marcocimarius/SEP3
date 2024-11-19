using grpc;
using Microsoft.OpenApi.Models;
using Via.Dk;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddControllers();
builder.Services.AddScoped<GrpcClient>();
var localPolicy = "LOCALHOST";
builder.Services.AddCors(options =>
{
    options.AddPolicy(name: localPolicy,
        policy =>
        {
            policy.WithOrigins("http://localhost:7153").AllowAnyHeader().AllowAnyMethod();
        });
});
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "My API", Version = "v1" });

    // Explicitly define schema for CreateRegistrationRequest
    c.MapType<CreateRegistrationRequest>(() => new OpenApiSchema
    {
        Type = "object",
        Properties = new Dictionary<string, OpenApiSchema>
        {
            { "Email", new OpenApiSchema { Type = "string"}},
            { "Password", new OpenApiSchema { Type = "string" } },
            { "IsAdmin", new OpenApiSchema { Type = "boolean" } },
        }
    });
});


var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
    app.MapGet("/", () => Results.Redirect("/swagger"));
}

app.UseCors(localPolicy);
app.MapControllers();
app.Run();