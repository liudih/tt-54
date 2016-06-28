package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import entity.manager.UserSkillMap;

public interface UserSkillMapMapper {
	@Select("select * from t_user_skill_map where iuserid = #{0}")
	List<UserSkillMap> getListByUserID(Integer userID);

	@Select("select * from t_user_skill_map where iuserid = #{0} and cskilltype = #{1}")
	List<UserSkillMap> getListByUserIDAndType(Integer userID, String type);

	@Select(" select * from t_user_skill_map where cskilltype = #{0} and iskillid = #{1}")
	List<UserSkillMap> getListByTypeAndSkill( String type,
			 Integer skillid);

	@Insert("insert into t_user_skill_map (cskilltype, iskillid, iuserid) values "
			+ "(#{cskilltype}, #{iskillid}, #{iuserid})")
	int insert(UserSkillMap map);

	@Delete("delete from t_user_skill_map where iuserid = #{0}")
	int deleteByUserID(Integer userID);
}
