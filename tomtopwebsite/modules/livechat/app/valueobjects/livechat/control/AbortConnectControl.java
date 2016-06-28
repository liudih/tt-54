package valueobjects.livechat.control;

/**
 * Origin cancel the connection request.
 * 
 * @author kmtong
 *
 */
public class AbortConnectControl extends Control {

	String destination;

	public AbortConnectControl() {
		super();
		setType(ControlType.ABORTCONNECT);
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}
