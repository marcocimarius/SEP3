using grpc;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddControllers();
builder.Services.AddScoped<GrpcClient>();

var app = builder.Build();
app.MapControllers();
app.UseHttpsRedirection();
app.Run();