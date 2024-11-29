namespace api.dto;

public class RegistrationDto
{
    public string Email { get; set; }
    public string Password { get; set; }
    public bool IsAdmin { get; set; }
}