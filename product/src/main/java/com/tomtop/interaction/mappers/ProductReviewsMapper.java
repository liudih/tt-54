package com.tomtop.interaction.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.ReviewCountAndScoreDto;
import com.tomtop.product.models.dto.ReviewTotalStartDto;
import com.tomtop.product.models.dto.review.InteractionCommentDto;
import com.tomtop.product.models.dto.review.ReviewPhotoVideoDto;

public interface ProductReviewsMapper {

	/**
	 * 获取商品评论等信息
	 * 
	 * @param listingId
	 * @return
	 */
	@Select({
			"<script>",
			"select clistingid as listingId,",
			"round(cast(avg(foverallrating) as numeric),1) avgScore,",
			"count(iid) as reviewCount",
			" from t_interaction_comment where istate=#{istate}",
			"  <if test=\"list !=null\">",
			"and clistingid in",
			"<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\"",
			"	separator=\",\" close=\")\">#{item}</foreach></if>",
			"group by clistingid", "</script>" })
	List<ReviewCountAndScoreDto> getScoreListByListingIds(
			@Param("list") List<String> listingId, @Param("istate") int istate);

	/**
	 * 获取商品评论星级及数量
	 * 
	 * @param listingId
	 * @return
	 */
	@Select({ "select clistingid as listingId,",
			"round(cast(avg(foverallrating) as numeric),1) avgScore,",
			"count(iid) as reviewCount",
			" from t_interaction_comment where istate=#{istate} ",
			"and clistingid=#{listingId}", "group by clistingid" })
	ReviewCountAndScoreDto getScoreByListingId(
			@Param("listingId") String listingId, @Param("istate") int istate);
	
	/**
	 * 获取商品1、2、3、4、5星级对应数量
	 * 
	 * @param listingId
	 * @return
	 */
	@Select({ "select ROUND(foverallrating) startNum,count(*) num from t_interaction_comment ",
			" where istate=#{istate} and clistingid=#{listingId}",
			"group by ROUND(foverallrating),clistingid" })
	List<ReviewTotalStartDto> getFoverallratingNumByListingId(
			@Param("listingId") String listingId, @Param("istate") int istate);
	/**
	 * 获取商品评论详情
	 * 
	 * @param listingId
	 * @return
	 */
	@Select({ 
		"select iid,clistingid,csku,cmemberemail,ccomment,iprice,iquality,ishipping,",
		 "iusefulness,foverallrating,dcreatedate,dauditdate,istate,iorderid,ccountry,",
		 "cplatform,iwebsiteid,ctitle from t_interaction_comment ",
		 "where clistingid=#{0} and istate=#{1} and iwebsiteid=#{2} ",
		 "ORDER BY dcreatedate desc LIMIT #{3}" })
	List<InteractionCommentDto> getInteractionCommentDtoByListingId(String listingId,
			Integer state,Integer siteId,Integer limit);
	
	/**
	 * 获取商品评论图片视频
	 * 
	 * @param listingId
	 * @return
	 */
	@Select({ "<script>",
		"select icommentid commentid,'photo' code,clistingid listingid,cimageurl url from t_interaction_product_member_photos ",
		"where iauditorstatus=#{status} and iwebsiteid=#{siteid} and icommentid in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ", 
		" UNION ",
		"select icomment commentid,'video' code,clistingid listingid,cvideourl url from t_interaction_product_member_video ",
		"where iauditorstatus=#{status} and iwebsiteid=#{siteid} and icomment in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>  ",
	    "ORDER BY commentid desc </script>"})
	List<ReviewPhotoVideoDto> getReviewPhotoVideoDtoByCommentId(@Param("status") Integer status,@Param("siteid") Integer siteid,@Param("list") List<Integer> commentId);
}
