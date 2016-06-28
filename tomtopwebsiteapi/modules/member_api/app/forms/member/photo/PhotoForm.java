package forms.member.photo;

import java.io.Serializable;

public class PhotoForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ccontenttype;

	public String getCcontenttype() {
		return ccontenttype;
	}

	public void setCcontenttype(String ccontenttype) {
		this.ccontenttype = ccontenttype;
	}
	
	
}
