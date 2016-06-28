package valueobjects.base;

/**
 * 
 * @ClassName: BaseContext
 * @Description: TODO(活动基础上下文)
 * @author yinfei
 * @date 2015年11月9日
 *
 */
public class ActivityBaseContext {
	
	private int languageId;
	private int websiteId;
	private String currency;
	
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	public int getWebsiteId() {
		return websiteId;
	}
	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
