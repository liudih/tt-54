package valueobjects.livechat;

public class AliasResolution {

	final String ltc;

	final boolean retryable;
	
	int allowNumber;

	public AliasResolution(String resultLTC, int allowNumber) {
		this.ltc = resultLTC;
		this.allowNumber = allowNumber;
		this.retryable = (resultLTC == null);
	}
	
	public AliasResolution(String resultLTC) {
		this.ltc = resultLTC;
		this.retryable = (resultLTC == null);
	}

	public AliasResolution(boolean retryable) {
		this.ltc = null;
		this.retryable = retryable;
	}

	public int getAllowNumber() {
		return allowNumber;
	}

	public void setAllowNumber(int allowNumber) {
		this.allowNumber = allowNumber;
	}

	public String getLTC() {
		return ltc;
	}

	public boolean isRetryable() {
		return retryable;
	}

	public boolean hasResult() {
		return ltc != null;
	}
}
