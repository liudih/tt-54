package services.interaction;

import java.util.List;

import javax.inject.Inject;

import dto.interaction.InteractionProductMemberPhotos;
import mapper.interaction.InteractionProductMemberPhotosMapper;

public class ShowOtherPhotosService 
{
	@Inject
	InteractionProductMemberPhotosMapper memberPhotosMapper;
	
	public List<InteractionProductMemberPhotos> getOhterphotos(Integer iid,String clistingid,String cemail)
	{
		List<InteractionProductMemberPhotos> getPhotos=memberPhotosMapper.getOhterSmallphotos(iid, clistingid, cemail);
		return getPhotos;
	}
}
