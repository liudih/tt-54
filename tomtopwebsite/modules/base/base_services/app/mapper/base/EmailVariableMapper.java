package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.EmailVariable;

public interface EmailVariableMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(EmailVariable record);

	int insertSelective(EmailVariable record);

	EmailVariable selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(EmailVariable record);

	int updateByPrimaryKey(EmailVariable record);

	@Select("select * from t_email_variable")
	List<EmailVariable> getAllEmailVariables();

	@Select("select * from t_email_variable where ctype=#{0}")
	List<EmailVariable> getEmailVariablesByType(String ctype);

	@Select("delete from t_email_variable where iid = #{0}")
	void deleteEmailVariableById(Integer variableid);

	@Select("select * from t_email_variable where iid=#{0}")
	EmailVariable getEmailVariableById(Integer variableid);

}