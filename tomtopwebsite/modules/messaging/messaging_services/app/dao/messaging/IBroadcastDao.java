package dao.messaging;

import java.util.List;
import java.util.Map;

import entity.messaging.Broadcast;

/**
 * 
 * @author lijun
 *
 */
public interface IBroadcastDao {

	/**
	 * 查询出t_message_broadcast表中的所有记录
	 * 
	 * @return List<Broadcast>
	 */
	public List<Broadcast> selectBroadcasts();

	/**
	 * 分页查询
	 * 
	 * @param page
	 *            要查询的页数
	 * @param PerPageNums
	 *            每页多少条数据
	 * @return List<Broadcast>
	 */
	public List<Broadcast> selectBroadcastsForPage(Map paras);

	/**
	 * 获取总页数
	 * 
	 * @param paras
	 *            参数
	 * @return
	 */
	public int getTotal(Map paras);

	/**
	 * 标记我的消息(删除 已阅)
	 * 
	 * @param paras
	 *            参数
	 * @return 1-成功 0-失败
	 */
	public int markMyBroadcastMessage(Map paras);

	/**
	 * 获取详情
	 * 
	 * @param paras
	 *            参数
	 * @return
	 */
	public Broadcast getDetail(Map paras);

	/**
	 * 新增消息
	 * 
	 * @param m
	 *            <code>Broadcast</code>
	 * @return
	 */
	public int add(Broadcast m);

	/**
	 * 更新操作
	 * 
	 * @param m
	 *            <code>Broadcast</code>
	 * @return
	 */
	public int update(Broadcast m);

	/**
	 * 推送消息
	 * 
	 * @param m
	 *            <code>Broadcast</code>
	 * @return
	 */
	public int publish(Broadcast m);

	/**
	 * 删除操作
	 * 
	 * @param paras
	 * 				参数
	 * @return
	 */
	public int delete(Map paras);
}
