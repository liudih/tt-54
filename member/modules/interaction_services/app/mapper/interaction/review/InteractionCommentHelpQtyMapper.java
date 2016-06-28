package mapper.interaction.review;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.interaction.InteractionCommentHelpQty;

public interface InteractionCommentHelpQtyMapper {
	int deleteByPrimaryKey(Integer iid);

	@Insert("insert into t_interaction_comment_help_qty"
			+ "(commentid,helpfulqty,nothelpfulqty) values"
			+ "(#{commentid}, #{helpfulqty}, #{nothelpfulqty})")
	int insert(InteractionCommentHelpQty record);

	int insertSelective(InteractionCommentHelpQty record);

	InteractionCommentHelpQty selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(InteractionCommentHelpQty record);

	@Update("update t_interaction_comment_help_qty set commentid=#{commentid},helpfulqty=#{helpfulqty}, nothelpfulqty=#{nothelpfulqty}, dcreatedate=#{dcreatedate} "
			+ "where iid=#{iid}")
	int updateByPrimaryKey(InteractionCommentHelpQty record);

	@Select("SELECT iid, commentid, helpfulqty,nothelpfulqty,dcreatedate FROM t_interaction_comment_help_qty WHERE commentid=#{commentid}")
	InteractionCommentHelpQty selectByCommentId(Integer commentId);

	@Select("select count(helpfulqty) from t_interaction_comment_help_qty"
			+ "  where commentid=#{commentid} and helpfulqty=1")
	int getHelpfulqtyCountByCommentid(Integer commentid);

	@Select("select count(nothelpfulqty) from t_interaction_comment_help_qty"
			+ "  where commentid=#{commentid} and nothelpfulqty=1")
	int getNothelpfulqtyCountByCommentid(Integer commentid);

}
