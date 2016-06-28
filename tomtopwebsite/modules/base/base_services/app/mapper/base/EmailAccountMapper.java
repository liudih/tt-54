package mapper.base;

import org.apache.ibatis.annotations.Select;

import dto.EmailAccount;

public interface EmailAccountMapper {
	int deleteByPrimaryKey(Integer iid);

    int insert(EmailAccount record);

    int insertSelective(EmailAccount record);

    EmailAccount selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(EmailAccount record);
    
    @Select("select * from t_email_account where  iwebsiteid =#{0} limit 1")
	EmailAccount getEmailAccountByIwebsiteid(Integer iwebsiteid);
    
	/**
	 * @param 根据邮件协议进行选择
	 * @return
	 */
	@Select("select * from t_email_account where  ctype =#{0}")
	EmailAccount getEmailAccountByCtype(String ctype);
}
