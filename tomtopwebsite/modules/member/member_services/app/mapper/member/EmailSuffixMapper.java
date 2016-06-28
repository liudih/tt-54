package mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.member.EmailSuffix;

public interface EmailSuffixMapper {

	@Select("select * from t_email_suffix where iid>#{0} order by iid asc")
	List<EmailSuffix> getMaxEmailSuffix(int mid);

	@Select("select max(iid) from t_email_suffix")
	int getEmailSuffixMaxId();

}
