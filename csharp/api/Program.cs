using grpc;

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
            policy.WithOrigins("http://localhost:5206").AllowAnyHeader().AllowAnyMethod();
        });
});

var app = builder.Build();
app.UseCors(localPolicy);
app.MapControllers();
app.Run();