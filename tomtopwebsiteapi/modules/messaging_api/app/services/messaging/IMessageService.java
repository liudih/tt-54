package services.messaging;

import valueobjects.base.Page;
import entity.messaging.Broadcast;

public interface IMessageService {

	/**
	 * 获取我的消息总条数,分页的时候会要用到
	 * 
	 * @return 我的
	 */
	public int getMyMessageTotal();

	/**
	 * 分页获取我的消息
	 * 
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            每页数据
	 * @return
	 */
	public Page<Broadcast> getMyMessageForPage(int page, int pageSize);

	/**
	 * 把我的消息设置为已阅状态
	 * 
	 * @param id
	 * @return
	 */
	public int readMessage(String id);

	public int deleteMessageByBroadcastId(String broadcastId);

	public int deleteMessage(String id);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	public Broadcast getDetail(String id);

	/**
	 * 表t_message_info中是否已经存在Broadcast消息
	 * 
	 * @param broadcastId
	 * @return
	 */
	public boolean isExistedByBroadcastId(String broadcastId);

	/**
	 * 插入操作
	 * @param paras
	 * @return
	 */
	public int insert(Broadcast paras);
}
