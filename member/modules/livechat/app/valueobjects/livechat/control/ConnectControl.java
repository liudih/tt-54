package valueobjects.livechat.control;

/**
 * Used to establish a chat session.
 * 
 * @author kmtong
 *
 */
public class ConnectControl extends Control {

	String destination;

	int ilanguageid;

	int iroleid;

	public ConnectControl() {
		super();
		setType(ControlType.CONNECT);
	}

	public ConnectControl(String destination) {
		this();
		this.destination = destination;
		setType(ControlType.CONNECT);
	}

	public ConnectControl(String destination, int ilanguageid, int iroleid) {
		this();
		this.destination = destination;
		this.ilanguageid = ilanguageid;
		this.iroleid = iroleid;
		setType(ControlType.CONNECT);
	}

	/**
	 * Destination Number (LTC or Alias) to connect to.
	 * 
	 * Warning: Giving LTC is potentially risky. Use alias (email) to connect is
	 * safer.
	 * 
	 * @return
	 */
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(int ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public int getIroleid() {
		return iroleid;
	}

	public void setIroleid(int iroleid) {
		this.iroleid = iroleid;
	}

}
