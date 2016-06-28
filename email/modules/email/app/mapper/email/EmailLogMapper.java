package mapper.email;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import entity.email.EmailLog;

public interface EmailLogMapper {
	@Insert({
		"insert into t_email_emaillog (cfromemail, ",
		"ctoemail, ctitle,ccontent,bsendstatus,cthirdresult,cfailreason) ",
		"values (#{cfromemail,jdbcType=VARCHAR}, ",
		"#{ctoemail,jdbcType=VARCHAR},#{ctitle,jdbcType=VARCHAR},#{ccontent,jdbcType=VARCHAR},#{bsendstatus,jdbcType=BOOLEAN},#{cthirdresult,jdbcType=VARCHAR},#{cfailreason,jdbcType=VARCHAR} )"})
	int insert(EmailLog emailLog);
	
	@Delete({ "delete from t_email_emaillog", "where dcreatedate< (now() - interval '7 day')" })
	int deleteRegular();
}
