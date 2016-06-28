package dto.image;

public enum FileLabelType {

	EDM("edm"), ACTIVITY("activity"), DEFAULT("default"),LOTTERY("lottery");

	private String FileLabelType;

	private FileLabelType(String fileLabelType) {
		FileLabelType = fileLabelType;
	}

	public String getFileLabelType() {
		return FileLabelType;
	}

}
