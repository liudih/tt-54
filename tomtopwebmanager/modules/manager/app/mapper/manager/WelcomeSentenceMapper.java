package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import entity.manager.WelcomeSentence;

public interface WelcomeSentenceMapper {

	@Select("SELECT cwelcomeSentence as welcomeSentence,ilanguageid as language from t_livechat_welcome_sentence where ilanguageid = #{0}")
	List<WelcomeSentence> getWelcomeSentenceByLanguage(int languageId);
}
