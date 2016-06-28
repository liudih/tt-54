package dto.product;

public enum HomePageShowHistoryTypeEnum {

	HOT("hot"), CLEARSTOCKS("clearstocks"), FEATURED("featured"), NEWARRIVALS(
			"new"), FREESHIPPING("freeShipping");

	public String type;

	private HomePageShowHistoryTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}