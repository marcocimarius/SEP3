package via.dk.model.auth;

public class CustomerInformation implements ICustomerInformation {
	private int registrationId;
	private String firstName;
	private String lastName;
	private String phone;
	private String streetName;
	private String streetNumber;
	private String cityName;
	private String postNumber;
	private String countryName;

	public CustomerInformation() {
		this.registrationId = 0;
		this.firstName = "";
		this.lastName = "";
		this.phone = "";
		this.streetName = "";
		this.streetNumber = "";
		this.cityName = "";
		this.postNumber = "";
		this.countryName = "";
	}

	public CustomerInformation(int registrationId, String firstName, String lastName, String phone, String streetName, String streetNumber, String cityName, String postNumber, String countryName) {
		this.registrationId = registrationId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.cityName = cityName;
		this.postNumber = postNumber;
		this.countryName = countryName;
	}

	public int getRegistrationId() {
		return registrationId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public String getCityName() {
		return cityName;
	}

	public String getPostNumber() {
		return postNumber;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setRegistrationId(int id) {
		this.registrationId = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setPostNumber(String postNumber) {
		this.postNumber = postNumber;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
