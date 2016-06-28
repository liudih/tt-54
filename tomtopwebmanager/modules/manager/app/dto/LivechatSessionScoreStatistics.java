package dto;

public class LivechatSessionScoreStatistics {

	private String userName;
	private Double sessionScore;
	private String latitude;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getSessionScore() {
		return sessionScore;
	}

	public void setSessionScore(Double sessionScore) {
		this.sessionScore = sessionScore;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}