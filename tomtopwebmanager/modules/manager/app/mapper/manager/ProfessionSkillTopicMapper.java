package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.manager.ProfessionSkillTopic;

public interface ProfessionSkillTopicMapper {
	@Select("select count(iid) from t_profession_skill_topic")
	int getCount();

	@Select("select * from t_profession_skill_topic where iid = #{0}")
	ProfessionSkillTopic getByID(int id);

	@Select("select * from t_profession_skill_topic order by iid desc limit #{1} offset (#{0} - 1) * #{1}")
	List<ProfessionSkillTopic> getPage(int p, int size);

	@Select("select * from t_profession_skill_topic where iskillid = #{2} order by iid desc limit #{1} offset (#{0} - 1) * #{1}")
	List<ProfessionSkillTopic> searchPage(int p, int size, int skillID);

	@Select("select * from t_profession_skill_topic where benable = true and ilanguageid = #{0}")
	List<ProfessionSkillTopic> getEnableTopicsByLanguage(int languageID);

	@Insert("insert into t_profession_skill_topic (iskillid, ctitle, cdescription, icreateuser, ilanguageid) "
			+ "values (#{iskillid}, #{ctitle}, #{cdescription}, #{icreateuser}, #{ilanguageid})")
	int insert(ProfessionSkillTopic topic);

	@Update("<script>update t_profession_skill_topic "
			+ "<set><if test=\"iskillid != null\" >iskillid = #{iskillid}, </if>"
			+ "<if test=\"ctitle != null\" >ctitle = #{ctitle}, </if>"
			+ "<if test=\"cdescription != null\" >cdescription = #{cdescription}, </if>"
			+ "<if test=\"benable != null\" >benable = #{benable}, </if>"
			+ "<if test=\"ilanguageid != null\" >ilanguageid = #{ilanguageid}, </if>"
			+ "</set> where iid = #{iid}</script>")
	int update(ProfessionSkillTopic topic);

	@Select("select count(iid) from t_profession_skill_topic where iskillid = #{0}")
	int searchCount(int skillID);

	@Select("select * from t_profession_skill_topic where benable = true")
	List<ProfessionSkillTopic> getAllEnableTopics();
}
