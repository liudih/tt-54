package services.payment;

import java.util.List;
import java.util.Map;

import extensions.payment.IPaymentHTMLPlugIn;
import extensions.payment.IPaymentProvider;

public interface IPaymentService {

	/**
	 * 获取Payment by id
	 *
	 * @param id
	 * @return 如果不存在，则返回null
	 * @author luojiaheng
	 */
	public abstract IPaymentProvider getPaymentById(String id);

	public abstract Map<String, IPaymentProvider> getMap();

	/**
	 * 根据类型，返回排序后的IPaymentHTMLPlugIn
	 *
	 * @return
	 * @author luojiaheng
	 */
	public abstract List<IPaymentHTMLPlugIn> getHTMLPlugIns(int type);

	public abstract List<String> filterByOrderTag(List<String> tags);

}