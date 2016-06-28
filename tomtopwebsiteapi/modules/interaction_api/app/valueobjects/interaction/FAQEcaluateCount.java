package valueobjects.interaction;

import java.io.Serializable;

public class FAQEcaluateCount implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Integer faqId;
	private Integer helpfulCount;
	private Integer notHelpfulCount;

	public Integer getFaqId() {
		return faqId;
	}

	public void setFaqId(Integer faqId) {
		this.faqId = faqId;
	}

	public Integer getHelpfulCount() {
		return helpfulCount;
	}

	public void setHelpfulCount(Integer helpfulCount) {
		this.helpfulCount = helpfulCount;
	}

	public Integer getNotHelpfulCount() {
		return notHelpfulCount;
	}

	public void setNotHelpfulCount(Integer notHelpfulCount) {
		this.notHelpfulCount = notHelpfulCount;
	}
}
