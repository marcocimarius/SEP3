package via.dk.model.auth;

public class Login implements ILogin {
	private String email;
	private String password;

	public Login() {
	}

	public Login(String email, String password) {
		this.email = email;
		this.password = password;
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
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}
}