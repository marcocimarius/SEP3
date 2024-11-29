package via.dk.model.auth;

public interface ILogin {
	public String getPassword();
	public String getEmail();
	public void setEmail(String email);
	public void setPassword(String password);
}
