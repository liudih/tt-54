package valueobjects.livechat.control;

public enum ControlType {

	PING, /* Ping */
	CONNECT, /* Initiator Action */
	RING, /* Acceptor Status */
	ABORTCONNECT, /* Initiator/Acceptor Action */
	ACCEPT, /* Acceptor Action */
	ESTABLISHED, /* Initiator/Acceptor Status */
	HANGUP, /* Initiator/Acceptor Action */
	CLOSED, /* Initiator/Acceptor Status */
	QUIT, /* Initiator/Acceptor Action */
	REFRESH, /*again connect*/
	LOGOUT, /*logout */
	WAIT,   /*wait*/
	TRANSFER /*transfer*/
}
