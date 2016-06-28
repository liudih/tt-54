package valueobjects.interaction;

import java.io.Serializable;
import java.util.List;

import dto.interaction.InteractionProductMemberPhotos;

public class MemberPhoto implements Serializable  {
	private static final long serialVersionUID = 1L;

	final List<InteractionProductMemberPhotos>  list;
	
	final String username;

	public List<InteractionProductMemberPhotos> getList() {
		return list;
	}

	public String getUsername() {
		return username;
	}

	public MemberPhoto(List<InteractionProductMemberPhotos> list,
			String username) {
		super();
		this.list = list;
		this.username = username;
	}
}
