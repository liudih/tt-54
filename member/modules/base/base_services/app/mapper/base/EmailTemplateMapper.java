package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.EmailTemplate;

public interface EmailTemplateMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(EmailTemplate record);

	int insertSelective(EmailTemplate record);

	EmailTemplate selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(EmailTemplate record);

	@Select("<script> select * from t_email_template "
			+ "where iwebsiteid= #{0} "
			+ "<if test=\"languageId !=0 \">and ilanguage=#{languageId}</if> "
			+ "<if test=\"ctype !='all' \">and ctype = #{ctype} </if> "
			+ "order by iid limit #{2} offset (#{1}-1)*#{2} </script>")
	List<EmailTemplate> getEmailTemplateByPages(Integer websiteid,
			Integer page, Integer pageSize, @Param("ctype")String ctype, @Param("languageId")Integer languageId);

	@Select("select count(iid) from t_email_template")
	Integer getEmailTemplateCount();

	@Select("select * from t_email_template where iid = #{templageid} limit 1")
	EmailTemplate getEmailTemplateById(Integer templageid);

	@Select("select * from t_email_template "
			+ "where ilanguage = #{0} and iwebsiteid=#{1} and ctype= #{2} limit 1")
	EmailTemplate getEmailTemplateBylangAndSiteAndType(Integer language,
			Integer siteId, String emailType);

}
