package dao.messaging;

import java.util.List;
import java.util.Map;

import entity.messaging.Broadcast;

/**
 * 我的消息dao
 * 
 * @author lijun
 *
 */
public interface IMessageDao {

	/**
	 * 获取我的消息总条数,分页的时候会要用到
	 * 
	 * @return 我的总消息数
	 */
	public int getMyMessageTotal(Map paras);

	/**
	 * 分页获取我的消息
	 * 
	 * @param paras
	 *            map
	 * @return List<Broadcast>
	 */
	public List<Broadcast> getMyMessageForPage(Map paras);

	/**
	 * 更新我的消息状态(已阅 or 删除)
	 * 
	 * @param paras
	 *            map
	 * @return 1:成功 0：失败
	 */
	public int updateMessageStatus(Map paras);

	/**
	 * 获取详情
	 * 
	 * @param paras
	 *            map
	 * @return Broadcast
	 */
	public Broadcast getDetail(Map paras);

	/**
	 * 记录是否已经存在
	 * 
	 * @param paras
	 *            map
	 * @return true or false
	 */
	public boolean isExisted(Map paras);

	/**
	 * 插入操作
	 * 
	 * @param paras
	 *            map
	 * @return 1:成功 0：失败
	 */
	public int insert(Broadcast paras);
}
