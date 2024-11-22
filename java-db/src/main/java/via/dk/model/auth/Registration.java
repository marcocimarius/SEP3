package via.dk.model.auth;

public class Registration implements IRegistration {
	private Integer id;
	private String email;
	private String password;
	private Boolean isAdmin;

	public Registration() {
	}

	public Registration(String email, String password, Boolean isAdmin) {
		this.id = null;
		this.email = email;
		this.password = password;
		this.isAdmin = isAdmin;
	}

	public Registration(Integer id, String email, String password, Boolean isAdmin) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.isAdmin = isAdmin;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public Boolean getIsAdmin() {
		return this.isAdmin;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
