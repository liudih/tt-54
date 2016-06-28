package valueobjects.product;

import java.io.Serializable;

/**
 * 获取广告时需要传入的参数对象
 * 
 * @author Administrator
 *
 */
public class ProductAdertisingContext implements Serializable{

	private static final long serialVersionUID = 3698171250908518760L;

	/**
	 * 唯一id (比如产品就传入 listingid)
	 */
	String businessId;

	/**
	 * 广告类型，如product(1) category(2) special（3） 存储在t_advertising_type表中
	 */
	Integer advertisingType;
	/**
	 * 站点
	 */
	Integer websiteId;
	/**
	 * 语言
	 */
	Integer languageId;
	/**
	 * 方位 广告存在于界面的位置，例如： left,right 存储在 t_advertising_positon表中
	 */
	Integer positonId;


	/**
	 * 设备： 如果，app/web/mobile
	 */
	String device;
	
	/**
	 * 获取广告需要传递此对象
	 * 
	 * @param businessId
	 *            - 唯一id (比如产品就传入 listingid)
	 * @param advertisingType
	 *            - 广告类型，如product（产品）special（专卖） 存储在t_advertising_type表中
	 * @param websiteId
	 *            -站点
	 * @param languageId
	 *            - 语言
	 * @param positonId
	 *            -方位 广告存在于界面的位置，例如： left,right 存储在 t_advertising_positon表中
	 */
	public ProductAdertisingContext(String businessId, Integer advertisingType,
			Integer websiteId, Integer languageId, Integer positonId) {
		super();
		this.businessId = businessId;
		this.advertisingType = advertisingType;
		this.websiteId = websiteId;
		this.languageId = languageId;
		this.positonId = positonId;
	}

	/**
	 * 获取广告需要传递此对象
	 * 
	 * @param businessId
	 *            - 唯一id (比如产品就传入 listingid)
	 * @param advertisingType
	 *            - 广告类型，如product（产品）special（专卖） 存储在t_advertising_type表中
	 * @param websiteId
	 *            -站点
	 * @param languageId
	 *            - 语言
	 * @param positonId
	 *            -方位 广告存在于界面的位置，例如： left,right 存储在 t_advertising_positon表中
	 * @param device
	 *            如:app/web/mobile 存储在v_host表 cdevice字段中
	 */
	public ProductAdertisingContext(String businessId, Integer advertisingType,
			Integer websiteId, Integer languageId, Integer positonId,
			String device) {
		super();
		this.businessId = businessId;
		this.advertisingType = advertisingType;
		this.websiteId = websiteId;
		this.languageId = languageId;
		this.positonId = positonId;
		this.device = device;
	}

	public String getBusinessId() {
		return businessId;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public Integer getAdvertisingType() {
		return advertisingType;
	}

	public Integer getPositonId() {
		return positonId;
	}

	public String getDevice() {
		return device;
	}

	@Override
	public String toString() {
		return "ProductAdertisingContext [businessId=" + businessId
				+ ", advertisingType=" + advertisingType + ", websiteId="
				+ websiteId + ", languageId=" + languageId + ", positonId="
				+ positonId + ", device=" + device + "]";
	}
	
	
}
