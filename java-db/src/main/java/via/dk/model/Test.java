package via.dk.model;

public class Test implements ITest{
	private Integer id;
	private String name;
	private String description;
	public Test() {
	}
	public Test(Integer id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public Integer getId() {
		return this.id;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}
