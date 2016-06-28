package valueobjects.livechat.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ControlCodec {

	static ObjectMapper om = new ObjectMapper();

	public static Control decode(JsonNode ctrl) {
		Control ctrlObj = null;
		ControlType type = ControlType.valueOf(ctrl.findPath("type")
				.textValue());
		switch (type) {
		case PING:
			ctrlObj = om.convertValue(ctrl, PingControl.class);
			break;
		case RING:
			ctrlObj = om.convertValue(ctrl, RingControl.class);
			break;
		case CONNECT:
			ctrlObj = om.convertValue(ctrl, ConnectControl.class);
			break;
		case ACCEPT:
			ctrlObj = om.convertValue(ctrl, AcceptControl.class);
			break;
		case ABORTCONNECT:
			ctrlObj = om.convertValue(ctrl, AbortConnectControl.class);
			break;
		case ESTABLISHED:
			ctrlObj = om.convertValue(ctrl, EstablishedControl.class);
			break;
		case HANGUP:
			ctrlObj = om.convertValue(ctrl, HangupControl.class);
			break;
		case CLOSED:
			ctrlObj = om.convertValue(ctrl, ClosedControl.class);
			break;
		case QUIT:
			ctrlObj = om.convertValue(ctrl, QuitControl.class);
			break;
		case REFRESH:
			ctrlObj = om.convertValue(ctrl, RefreshControl.class);
			break;
		case LOGOUT:
			ctrlObj = om.convertValue(ctrl, LogoutControl.class);
			break;
		case WAIT:
			ctrlObj = om.convertValue(ctrl, WaitControl.class);
			break;
		case TRANSFER:
			ctrlObj = om.convertValue(ctrl, TransferControl.class);
			break;
		default:

		}
		return ctrlObj;
	}

	public static JsonNode encode(Control c) {
		return om.convertValue(c, JsonNode.class);
	}
}
