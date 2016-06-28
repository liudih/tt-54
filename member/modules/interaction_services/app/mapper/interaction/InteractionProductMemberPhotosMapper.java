package mapper.interaction;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.interaction.InteractionProductMemberPhotos;

public interface InteractionProductMemberPhotosMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(InteractionProductMemberPhotos record);

	int insertSelective(InteractionProductMemberPhotos record);

	InteractionProductMemberPhotos selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(InteractionProductMemberPhotos record);

	int updateByPrimaryKey(InteractionProductMemberPhotos record);

	@Select("SELECT * from t_interaction_product_member_photos  WHERE clistingid = #{0} and icommentid is null and iauditorstatus=1 ")
	List<InteractionProductMemberPhotos> getProductMemberPhotoByListingId(String clistingid);

	@Select("select cimageurl from t_interaction_product_member_photos where icommentid = #{0} and iauditorstatus=1")
	List<String> getCommentImgageByCommentId(Integer commentId);

	@Select("select cimageurl from t_interaction_product_member_photos where icommentid = #{0} order by dcreatedate desc limit 5")
	List<String> getCommentImgageByCommentIdLimit5(Integer commentId);

	@Select("select * from t_interaction_product_member_photos WHERE clistingid = #{0} ")
	List<InteractionProductMemberPhotos> getlistPhotos(String clistingid);

	@Select("select b.cmemberemail from t_interaction_product_member_photos b where b.clistingid=#{0} and icommentid is null and iauditorstatus=1 group by b.cmemberemail")
	List<InteractionProductMemberPhotos> getMemberEmail(String clistingid);

	@Select("select b.* from t_interaction_product_member_photos b where b.iid not in(#{0})"
			+ "and b.clistingid=#{1} and b.cmemberemail=#{2} and icommentid is null and iauditorstatus=1")
	List<InteractionProductMemberPhotos> getOhterSmallphotos(Integer iid,
			String clistingid, String email);

	@Select("select * from t_interaction_product_member_photos where cemail=#{0} and icommentid is null and iauditorstatus=1")
	InteractionProductMemberPhotos getAllByEmail(String email);

	int batchInsert(Map<String, List<InteractionProductMemberPhotos>> map);
	
	@Select("select * from t_interaction_product_member_photos where iid=#{0} and icommentid is null and iauditorstatus=1")
	InteractionProductMemberPhotos getPhotoById( int id);
	
	@Select({"<script>",
			"select * from t_interaction_product_member_photos ",
			"<if test=\"st!=-1\">",
			"where iauditorstatus=#{st} ",
			"</if>",
			"order by iid desc limit #{1} offset #{0}",
			"</script>"})
    List<InteractionProductMemberPhotos> getPhotoPage(Integer page,Integer pageSize,@Param("st")int status);
    
    @Select({"<script>",
		"select count(*) from t_interaction_product_member_photos ",
		"<if test=\"st!=-1\">",
		"where iauditorstatus=#{st} ",
		"</if>",
		"</script>"})
    int getPhotoCount(@Param("st")int status);
    
    @Select("select count(*) from t_interaction_product_member_photos where "+
    		"clistingid=#{1} and cmemberemail=#{0} and icommentid is null")
    int getPhotoCheckNum(String email,String listingid);
	
}