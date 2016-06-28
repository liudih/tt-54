package dto.product;

public enum AttachmentTypeEnum {

	DRIVE("drive"), PDF("pdf");

	public String type;

	private AttachmentTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}