package services.product;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import play.Logger;

import com.google.common.eventbus.EventBus;

import dao.IStorageDao;
import dao.IStorageNameMappingDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductStorageMapEnquiryDao;
import dto.ProductUpdateInfo;
import dto.product.ProductBase;
import dto.product.ProductLabel;
import events.product.ProductUpdateEvent;

public class UpdateProductStatusAndQuantityAndLabelService {

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	ProductLabelService productlabelservice;

	@Inject
	IProductStorageMapEnquiryDao productStorageMapEnquirDao;

	@Inject
	IStorageNameMappingDao IStorageNameMappingDao;

	@Inject
	IStorageDao storageDao;

	@Inject
	EventBus eventBus;

	@Inject
	IProductBaseEnquiryDao productBaseEnquiryDao;

	final Integer STOP_SALE_STATUS = 3; // product_base下架状态

	final Integer ON_SALE_STATUS = 1; // product_base在售状态

	/**
	 * updateProductStatusAndQuantityAndLabel(更新产品状态，数量，标签) jiang
	 */
	public void updateProductStatusAndQuantityAndLabel(
			List<ProductUpdateInfo> productUpdateInfos) {
		if (null != productUpdateInfos && productUpdateInfos.size() > 0) {
			// 设置listingId在product_storage_mapping中是否可用，available
			for (ProductUpdateInfo productUpdateInfo : productUpdateInfos) {
				// erp的仓库名称
				String status = productUpdateInfo.getStatus();
				if (null != status && "stopsale".equals(status)) {
					String storageName = productUpdateInfo.getStorageName();
					String listingId = productUpdateInfo.getListingId();
					Boolean available = false;
					// 网站对应的仓库名称
					String storageName2 = null;
					if (null != storageName) {
						storageName2 = IStorageNameMappingDao
								.getStorageNameByStorageName(storageName);
					}
					Integer storageId = null;
					if (null != storageName2) {
						storageId = storageDao
								.getStorageIdByStorageName(storageName2);
					}
					if (null != storageId) {
						productStorageMapEnquirDao
								.UpdateStatusByStorageIdAndListingId(available,
										storageId, listingId);
					}
				}
			}

			for (ProductUpdateInfo productUpdateInfo : productUpdateInfos) {
				String storageName = productUpdateInfo.getStorageName();
				// 只有SZ1和GZ1是有效的，其他仓库不考虑
				if (null != storageName
						&& ("SZ1".equals(storageName) || "GZ1"
								.equals(storageName))) {
					Integer websiteId = productUpdateInfo.getWebsiteId();
					Integer quantity = productUpdateInfo.getQuantity();
					String status = productUpdateInfo.getStatus();
					String listingId = productUpdateInfo.getListingId();
					String type = productUpdateInfo.getProductType();

					if (null != status && "stopsale".equals(status)) {
						// 当所有仓库的该产品的总数
						Integer count1 = productStorageMapEnquirDao
								.getProductStorageMapsCountByListingId(listingId);

						Boolean bavailable = false;
						// 当所有仓库的该产品不可用的数量
						Integer count2 = productStorageMapEnquirDao
								.getProductStorageMapsCountByListingIdAndType(
										listingId, bavailable);

						if (count1 == count2) {
							// 10是停售的状态，是停售我们将产品状态改为下架
							productBaseMapper.updateStatusAndQuantity(
									websiteId, listingId, STOP_SALE_STATUS,
									quantity);

							// 停售，则将关于这个产品的所有标签给清除
							productlabelservice
									.deleteProductLabelByListingIdAndSiteId(
											listingId, websiteId);
							// 处理搜索引擎该listingId的索引
							deleteProductIndexing(listingId);
						}
					} else {
						// erp其他状态是（常售，清货，促销，编辑）,如果erp传过来的产品在网站是非在售状态，则不处理,为在线的则处理
						Integer productStatus = productBaseMapper
								.getProductStatusByListingId(listingId);
						if (ON_SALE_STATUS == productStatus) {
							int updateResult = productBaseMapper
									.updateProductQty(quantity, listingId);
							if (updateResult > 0) {
								// 修改产品标签，存在则不处理，不存在则插入
								Boolean isExsit = productlabelservice
										.getProductLabelByListingIdAndTypeAndSite(
												listingId, websiteId, type);
								if (isExsit == false) {
									ProductLabel productLabel = new ProductLabel();
									productLabel.setClistingid(listingId);
									productLabel.setIwebsiteid(websiteId);
									productLabel.setCtype(type);
									productLabel.setCcreateuser("erp");
									boolean r = productlabelservice
											.addProductLabel(productLabel);
									if (r == false) {
										Logger.debug("update prouduct label error ,listingid is  "
												+ listingId);
									}
								}
								eventBus.post(new ProductUpdateEvent(
										null,
										listingId,
										ProductUpdateEvent.ProductHandleType.update));
							} else {
								Logger.debug("update prouduct quantity error ,listingid is "
										+ listingId);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * deleteProductIndexing(删除索引)
	 * 考虑因素listingId是否符合多属性，是否主产品，如果是，在处理delete操作时，还得另外新建一个主产品 jiang
	 */
	public void deleteProductIndexing(String listingId) {
		ProductBase productBase = productBaseEnquiryDao
				.getProductBaseByListingId(listingId);
		if (null != productBase) {
			Boolean isMain = null != productBase.getBmain() ? productBase
					.getBmain() : false;
			String parentSku = productBase.getCparentsku();
			if (isMain && null != parentSku) {
				List<String> childrenListings = productBaseEnquiryDao
						.getChildrenListingIdByParentSku(parentSku);
				if (null != childrenListings && childrenListings.size() > 0) {
					String newMainListingId = childrenListings.get(0);
					if (listingId.equals(newMainListingId)
							&& childrenListings.size() > 1) {
						newMainListingId = childrenListings.get(1);
					}
					productBaseEnquiryDao.updateProductMainAndVisible(
							newMainListingId, true, true);
					productBaseEnquiryDao.updateProductMainAndVisible(
							listingId, false, false);
					// 新加一个主产品的索引
					eventBus.post(new ProductUpdateEvent(null,
							newMainListingId,
							ProductUpdateEvent.ProductHandleType.update));
				}
			}
			// 删除索引
			eventBus.post(new ProductUpdateEvent(null, listingId,
					ProductUpdateEvent.ProductHandleType.delete));
		}
	}
}
