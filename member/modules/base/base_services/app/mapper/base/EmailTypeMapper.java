package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.EmailType;

public interface EmailTypeMapper {
	@Select("select * from t_email_type")
	List<EmailType> getAllEmailType();

}