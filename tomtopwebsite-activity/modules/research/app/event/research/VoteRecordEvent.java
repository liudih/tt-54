package event.research;

public class VoteRecordEvent {

	private int votePageItemId;
	private String email;
	private String vhost;
	private int websiteId;

	public VoteRecordEvent(int votePageItemId, String email, String vhost,
			int websiteId) {
		super();
		this.votePageItemId = votePageItemId;
		this.email = email;
		this.vhost = vhost;
		this.websiteId = websiteId;
	}

	public int getVotePageItemId() {
		return votePageItemId;
	}

	public String getEmail() {
		return email;
	}

	public String getVhost() {
		return vhost;
	}

	public int getWebsiteId() {
		return websiteId;
	}

}
