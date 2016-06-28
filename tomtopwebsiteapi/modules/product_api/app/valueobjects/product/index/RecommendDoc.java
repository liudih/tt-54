package valueobjects.product.index;

public class RecommendDoc {
	Integer categoryId;
	int sequence;
	int siteid;
	String device;
	public RecommendDoc(Integer categoryId, int sequence, int siteid, String device) {
		super();
		this.categoryId = categoryId;
		this.sequence = sequence;
		this.siteid = siteid;
		this.device = device;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getSiteid() {
		return siteid;
	}
	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
}
