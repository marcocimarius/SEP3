package via.dk.model.auth;

/**
 * Model class for the Login object.
 */
public class Login implements ILogin {
	private String email;
	private String password;

	public Login() {
	}

	/**
	 * Constructor for the Login class.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 */
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