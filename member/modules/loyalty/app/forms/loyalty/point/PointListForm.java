package forms.loyalty.point;

public class PointListForm {
	Integer pageSize;
	Integer pageNum;
	Integer status;
	String expireDate;
	String source;
	Integer unUsePoints;
	Integer usedPoints;
	
	public Integer getPageSize() {
		return pageSize != null ? pageSize : 10;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum != null ? pageNum : 1;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getUnUsePoints() {
		return unUsePoints;
	}

	public void setUnUsePoints(Integer unUsePoints) {
		this.unUsePoints = unUsePoints;
	}

	public Integer getUsedPoints() {
		return usedPoints;
	}

	public void setUsedPoints(Integer usedPoints) {
		this.usedPoints = usedPoints;
	}
	
	
}
