package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.manager.ProfessionSkill;

public interface ProfessionSkillMapper {
	@Select("select count(iid) from t_profession_skill")
	int getCount();

	@Select("select * from t_profession_skill limit #{1} offset (#{0}-1)*#{1}")
	List<ProfessionSkill> getPage(int page, int pageSize);

	@Update("update t_profession_skill set cskillname = #{0}  where iid = #{1}")
	int updateSkillNameByID(String name, int id);

	@Insert("insert into t_profession_skill (cskillname) values (#{0})")
	int insert(String skillName);

	@Delete("delete from t_profession_skill where iid = #{0}")
	int deleteByID(int id);

	@Select("select * from t_profession_skill")
	List<ProfessionSkill> getAll();

	@Select("select * from t_profession_skill where iid = #{0} limit 1")
	ProfessionSkill getByID(int id);
	
	@Select("select * from t_profession_skill where cskillname = #{0} limit 1")
	ProfessionSkill getByName(String name);
	
}
