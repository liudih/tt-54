package services.interaction;

import java.util.List;

import javax.inject.Inject;

import dto.interaction.InteractionProductMemberPhotos;
import mapper.interaction.InteractionProductMemberPhotosMapper;
import valueobjects.product.IProductFragment;

public class InteractionProductPhotosService implements IProductFragment{
	
	@Inject
	InteractionProductMemberPhotosMapper productMemberPhotosMapper;
	
	
	public List<InteractionProductMemberPhotos> getAll(String clistingid)
	{
		List<InteractionProductMemberPhotos> l=productMemberPhotosMapper.getlistPhotos(clistingid);
		return l;
	}

}