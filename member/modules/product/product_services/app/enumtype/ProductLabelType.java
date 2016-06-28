package enumtype;

public enum ProductLabelType {

	NewArrial("new"), Hot("hot"), Featured("featured"), Clearstocks(
			"clearstocks"), Special("special"), FreeShipping("freeShipping"), DailyDeals(
			"dailydeals"), Dodocool("dodocool");

	private String typeName;

	private ProductLabelType(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return this.typeName;
	}
}