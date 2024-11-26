package via.dk.model.auth;

public interface ICustomerInformation {
	public String getFirstName();

	public String getLastName();

	public String getPhone();

	public String getStreetName();

	public String getStreetNumber();

	public String getCityName();

	public String getPostNumber();

	public String getCountryName();

	public void setFirstName(String str);

	public void setLastName(String str);

	public void setPhone(String str);

	public void setStreetName(String str);

	public void setStreetNumber(String str);

	public void setCityName(String str);

	public void setPostNumber(String str);

	public void setCountryName(String str);
}
