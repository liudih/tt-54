package forms;

import play.data.validation.Constraints.Required;

public class CustomerServiceScoreForm {
	@Required
	private Integer p;
	private String name;
	private Integer typeID;

	public Integer getP() {
		return p;
	}

	public void setP(Integer p) {
		this.p = p;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTypeID() {
		return typeID;
	}

	public void setTypeID(Integer typeID) {
		this.typeID = typeID;
	}

}
