namespace api.Responses;

public class ApiLoginResponse
{
   public required int Id { get; set; }
   public required bool IsAdmin { get; set; }
    public required string Email { get; set; }
}