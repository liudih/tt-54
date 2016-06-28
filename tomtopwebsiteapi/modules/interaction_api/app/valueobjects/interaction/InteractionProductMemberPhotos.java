package valueobjects.interaction;

import java.util.List;

import valueobjects.product.IProductFragment;

public class InteractionProductMemberPhotos implements IProductFragment{
	
	final List<MemberPhoto> photos;

	public List<MemberPhoto> getPhotos() {
		return photos;
	}

	public InteractionProductMemberPhotos(List<MemberPhoto> photos) {
		this.photos = photos;
	}

}