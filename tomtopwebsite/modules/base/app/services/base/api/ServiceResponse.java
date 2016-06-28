package services.base.api;

public class ServiceResponse {
	private Double version;
	private Integer code;
	private String ack;
	private String description;
	private Object result;

	public ServiceResponse() {
		this.version = Double.valueOf(1.0D);
		this.code = ServiceResponseCode.SUCCESS;
	}

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getResult() {
		return this.result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Double getVersion() {
		return this.version;
	}

	public String getAck() {
		return ack;
	}

	public void setAck(String ack) {
		this.ack = ack;
	}
	
	
}
