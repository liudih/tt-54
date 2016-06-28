package dto.advertising;

import java.io.Serializable;

import context.WebContext;

/**
 * 获取广告时需要传入的参数对象
 * 
 * @author Administrator
 *
 */
public class ProductAdertisingContextExtended implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 唯一id (比如产品就传入 listingid)
	 */
	String businessId;

	/**
	 * 广告类型，如product(1) category(2) special（3） 存储在t_advertising_type表中
	 */
	Integer advertisingType;

	/**
	 * 方位 广告存在于界面的位置，例如： left,right 存储在 t_advertising_positon表中
	 */
	Integer positonId;

	WebContext context;

	/**
	 * 获取广告需要传递此对象
	 * 
	 * @param businessId
	 *            - 唯一id (比如产品就传入 listingid)
	 * @param advertisingType
	 *            - 广告类型，如product（产品）special（专卖） 存储在t_advertising_type表中
	 * @param positonId
	 *            -方位 广告存在于界面的位置，例如： left,right 存储在 t_advertising_positon表中
	 * @param context 外部项目 调用接口
	 */
	public ProductAdertisingContextExtended(String businessId,
			Integer advertisingType, Integer positonId, WebContext context) {
		super();
		this.businessId = businessId;
		this.advertisingType = advertisingType;
		this.positonId = positonId;
		this.context = context;
	}

	public String getBusinessId() {
		return businessId;
	}

	public Integer getAdvertisingType() {
		return advertisingType;
	}

	public Integer getPositonId() {
		return positonId;
	}

	public WebContext getContext() {
		return context;
	}

}
