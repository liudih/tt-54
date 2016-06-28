package services.activity.page;

/**
 * 活动参与服务接口类
 * 
 * @author Guozy
 *
 */
public interface IPageJoinService {

	/**
	 * 根据cemail分组，获取参与活动的所有数据信息的数量
	 * 
	 * @return
	 */
	public Integer getPageJoinsBycjoinerAndIpageId(String cjoiner,
			Integer ipageid);
}
