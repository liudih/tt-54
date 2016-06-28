package mapper.interaction.review;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import valueobjects.interaction.ReviewHelpEvaluateCount;
import dto.interaction.InteractionCommentHelpEvaluate;

public interface InteractionCommentHelpEvaluateMapper {

	@Insert("insert into t_interaction_comment_help_evaluate"
			+ "(icommentid, helpfulcode, cmemberemail) values"
			+ "(#{icommentid}, #{helpfulcode}, #{cmemberemail})")
	int insert(InteractionCommentHelpEvaluate record);

	int insertSelective(InteractionCommentHelpEvaluate record);

	List<ReviewHelpEvaluateCount> getHelpfulCount(List<Integer> list);

	@Select("select count(cmemberemail) from t_interaction_comment_help_evaluate where icommentid = #{0} and cmemberemail = #{1}")
	int validateEvaluteExist(Integer commentId, String cmemberemail);

	@Select("select * from t_interaction_comment_help_evaluate where icommentid = #{0} and cmemberemail = #{1}")
	List<InteractionCommentHelpEvaluate> getEvaluteList(Integer commentId, String email);

}
