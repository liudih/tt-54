package extensions.livechat.topic;

public class WelcomeSentence {

	private String language;

	private String welcomeSentence;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getWelcomeSentence() {
		return welcomeSentence;
	}

	public void setWelcomeSentence(String welcomeSentence) {
		this.welcomeSentence = welcomeSentence;
	}

	public WelcomeSentence(String language, String welcomeSentence) {
		super();
		this.language = language;
		this.welcomeSentence = welcomeSentence;
	}

	public WelcomeSentence() {
		super();
	}

}
