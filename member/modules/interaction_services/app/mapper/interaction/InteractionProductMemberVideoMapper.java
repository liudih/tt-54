package mapper.interaction;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.interaction.InteractionProductMemberVideo;

public interface InteractionProductMemberVideoMapper {
    int deleteByPrimaryKey(Integer iid);
    
    @Insert("insert into t_interaction_product_member_video(clistingid,csku,cmemberemail,icomment,cvideourl,clabel,"
    		+ "iauditorstatus) "
    		+ "values (#{clistingid},#{csku},#{cmemberemail},#{icomment},#{cvideourl},#{clabel},#{iauditorstatus})")
    int insert(InteractionProductMemberVideo record);

    int insertSelective(InteractionProductMemberVideo record);

    InteractionProductMemberVideo selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(InteractionProductMemberVideo record);

    int updateByPrimaryKey(InteractionProductMemberVideo record);
    
    @Select("select b.cmemberemail,b.cvideourl cvideourl from t_interaction_product_member_video b "+
    		"where clistingid=#{0} and icomment is null and iauditorstatus=1 "+
    		"order by dcreatedate desc limit 21")
    List<InteractionProductMemberVideo> getMemberVideosBylistId(String clistingid);
    
    @Select("select * from t_interaction_product_member_video where clistingid=#{1} and icomment is null and iauditorstatus=1")
    List<InteractionProductMemberVideo> getMemberVideos(String clistingid);
    
    @Select("select b.cmemberemail from t_interaction_product_member_video b where b.clistingid=#{0} and icomment is null and iauditorstatus=1 group by b.cmemberemail")
    List<InteractionProductMemberVideo> getMemberEmail(String clistingid);

    @Select("select cvideourl from t_interaction_product_member_video where icomment = #{0} and iauditorstatus=1 order by dcreatedate desc limit 1")
	String getCommentVideoByCommentIdLimit1(Integer commentId);
    
    @Select({"<script>",
    	"select * from t_interaction_product_member_video ",
    	"<if test=\"st!=-1\">",
    	"where iauditorstatus=#{st} ",
    	"</if>",
    	"order by iid desc limit #{1} offset #{0}",
    	"</script>"})
    List<InteractionProductMemberVideo> getVideoPage(Integer page,Integer pageSize,@Param("st")int status);
    
    @Select({"<script>",
    	"select count(*) from t_interaction_product_member_video ",
    	"<if test=\"st!=-1\">",
    	"where iauditorstatus=#{st} ",
    	"</if>",
    	"</script>"})
    int getVideoCount(@Param("st")int status);
    
    @Select("select count(*) from t_interaction_product_member_video where "+
    		"icomment is null and cmemberemail=#{0} and clistingid=#{1} "+
    		"AND dcreatedate BETWEEN #{2} AND #{3} ")
    int getVideoNum(String email,String listingid,
    		Date from, Date to);
} 