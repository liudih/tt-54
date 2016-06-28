package services.interaction;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import mapper.interaction.InteractionProductMemberVideoMapper;

import com.google.inject.Inject;

import dto.interaction.InteractionProductMemberVideo;
import valueobjects.product.IProductFragment;

public class InteractionProductVideoService implements IProductFragment
{
	@Inject
	InteractionProductMemberVideoMapper memberVideoMapper;
	
	public List<InteractionProductMemberVideo> getAll(String clistingid)
	{
		List<InteractionProductMemberVideo> listvideos=memberVideoMapper.getMemberVideos(clistingid);
		return listvideos;
	}
	
	public int savaVideo(InteractionProductMemberVideo mVideo){
		int num=memberVideoMapper.insertSelective(mVideo);
		return num;
	}
	
	public boolean checkVideoNum(String email,String listingid){
		Date from = DateUtils.truncate(new Date(), Calendar.DATE);
		Date to = DateUtils.addMilliseconds(DateUtils.addDays(from, 1), -1);
		int num = memberVideoMapper.getVideoNum(email,listingid,from,to);
		return num<5;
	}
}
