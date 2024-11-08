package via.dk.model;

public interface IRegistration {
	public String getEmail();
	public String getPassword();
	public Boolean getIsAdmin();
	public void setEmail(String email);
	public void setPassword(String password);
	public void setIsAdmin(Boolean isAdmin);
}
