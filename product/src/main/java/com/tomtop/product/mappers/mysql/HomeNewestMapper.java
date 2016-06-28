package com.tomtop.product.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.HomeNewestDto;

public interface HomeNewestMapper {

	@Select({"select 'review' type,review_content content,title,listing_id listingId,sku,review_by userBy,country "
			,"from home_newest_review  where client_id=#{0} and language_id=#{1} and is_enabled=1 and is_deleted=0 "
			,"UNION select 'image' type,img_url content,title,listing_id listingId,sku,img_by userBy,country "
			,"from home_newest_image where client_id=#{0} and language_id=#{1} and is_enabled=1 and is_deleted=0 "
			,"UNION select 'video' type,video_url content,title,listing_id listingId,sku,video_by userBy,country "
			,"from home_newest_video where client_id=#{0} and language_id=#{1} and is_enabled=1 and is_deleted=0 "})
	List<HomeNewestDto> getHomeNewest(int client, int lang);
}
