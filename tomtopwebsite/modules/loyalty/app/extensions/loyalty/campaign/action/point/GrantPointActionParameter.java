package extensions.loyalty.campaign.action.point;

import services.campaign.IActionParameter;

public class GrantPointActionParameter implements IActionParameter {

	int points;

	int status = 1;

	String source;

	double rate;

	@Override
	public String getActionId() {
		return GrantPointAction.ID;
	}

	public void setActionId(String actionId) {
		// no-op
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
