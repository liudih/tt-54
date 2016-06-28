package services.interaction;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;

import play.Logger;
import services.base.FoundationService;
import session.ISessionService;

public class ProductReviewService {
	@Inject
	ISessionService sessionService;
	
	@Inject
	FoundationService foundationService;
	
	final String guid = "TYPEIID";

	final String countid = "countid";
	
	public void pushPreveiwImageWithSession(String file, int index,
			String contentType) {
		String sessionid = foundationService.getSessionID();
		String fileid = sessionid + index;
		String contenttypeid = sessionid + guid + index;
		String countID = sessionid + countid;
		sessionService.set(countID, index);
		sessionService.set(fileid, file);
		sessionService.set(contenttypeid, contentType);
		this.clearPreveiwImageCount();
	}
	
	public void clearPreveiwImageCount() {
		String sessionid = foundationService.getSessionID();
		String countID = sessionid + countid;
		sessionService.remove(countID);
		sessionService.set(countID, 0);
	}
	public byte[] getPreveiwImageWithSession(int index) {
		String sessionid = foundationService.getSessionID();
		String fileid = sessionid + index;
		String img = (String)sessionService.get(fileid);
		//Logger.debug("++img+"+img);
		byte[] b=img.getBytes(); 
		Base64 base64=new Base64();
		b=base64.decode(b);
		return b;
	}
}
