package via.dk.model.auth;

public interface IRegistration {
	public Integer getId();
	public String getPassword();
	public String getEmail();
	public Boolean getIsAdmin();
	public void setEmail(String email);
	public void setPassword(String password);
	public void setIsAdmin(Boolean isAdmin);
}
