package mapper.messaging;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.messaging.Broadcast;
import entity.messaging.MessageInfo;

/**
 * 我的消息映射类
 * 
 * @author lijun
 *
 */
public interface MessageMapper {

	/**
	 * 获取我的消息总条数,分页的时候会要用到
	 * 
	 * @return
	 */
	public int getMyMessageTotal(Map paras);

	/**
	 * 分页获取我的消息
	 * 
	 * @param paras
	 * @return
	 */
	public List<Broadcast> getMyMessageForPage(Map paras);

	/**
	 * 更新我的消息状态(已阅 or 删除)
	 * 
	 * @param paras
	 * @return
	 */
	public int updateMessageStatus(Map paras);

	/**
	 * 获取详情
	 * 
	 * @param paras
	 * @return
	 */
	public Broadcast getDetail(Map paras);

	/**
	 * 记录是否已经存在
	 * 
	 * @param paras
	 * @return
	 */
	public int isExisted(Map paras);

	/**
	 * 插入操作
	 * @param paras
	 * @return
	 */
	public int insert(Broadcast paras);
	
	
	
	/**
	 * 获取针对个人发送信息列表
	 * @auther CJ
	 * @return
	 */
	@Select({ "<script>", "select * from t_message_info where 1 =1",
			" and isendid = #{adminId}",
			" order by dcreatedate desc limit #{pz} offset #{pg}", "</script>" })
	List<MessageInfo> getAllPersonalMessages(@Param("pg") int pageIndex,@Param("pz") int pageSize, @Param("adminId") int adminUserId);
	
	
	/**
	 * 统计记录数
	 * @auther CJ
	 * @return
	 */
	@Select({
		"<script>",
			"select count(*) from t_message_info where isendid = #{adminId}",		
		"</script>" })
	int getCountPersonalMessages(@Param("adminId") int adminUserId);	
}
