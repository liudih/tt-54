package extensions.member;

/**
 * 我的消息
 * @author lijun
 *
 */
public interface IMyMessageProvider {

	/**
	 * 获取未读消息总数
	 * @return
	 */
	public int getUnreadTotal();
}
