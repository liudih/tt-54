package forms;

import play.data.validation.Constraints.Required;

public class ExchangeRateForm {
	@Required
	private Integer id;
	@Required
	private String code;
	@Required
	private Double rate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

}
