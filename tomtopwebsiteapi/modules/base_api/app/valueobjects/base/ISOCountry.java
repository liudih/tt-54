package valueobjects.base;

public class ISOCountry {

	final int id;
	final String name;
	final String isoCode;

	public ISOCountry(int id, String name, String isoCode) {
		super();
		this.id = id;
		this.name = name;
		this.isoCode = isoCode;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIsoCode() {
		return isoCode;
	}

}
