package services.product;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductBaseTranslateMapper;
import valueobjects.product.ProductBaseTranslate;
import dao.product.IProductTranslateEnquiryDao;
import dao.product.IProductTranslateUpdateDao;
import dao.product.IProductUrlEnquiryDao;
import dto.product.ProductTranslate;
import dto.product.ProductUrl;

public class ProductBaseTranslateService {

	@Inject
	ProductBaseTranslateMapper productbTranslateMapper;

	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;
	
	@Inject
	IProductTranslateEnquiryDao productTranslateEnquityDao;
	
	@Inject
	IProductTranslateUpdateDao productTranslateUpdateDao;

	public List<ProductBaseTranslate> getAllTranslate() {
		return productbTranslateMapper.getAllTranslate();
	}

	public List<ProductBaseTranslate> getAllTranslatePaged(int page,
			int pageSize,int websiteId) {
		return productbTranslateMapper.getPagedAllTranslate(page * pageSize,
				pageSize,websiteId);
	}

	public int getAllTranslateCount(Integer websiteId) {
		return productbTranslateMapper.getAllTranslateCount(websiteId);
	}

	public List<ProductBaseTranslate> getTranslatePaged(
			List<String> listingIds, int page, int pageSize) {
		return productbTranslateMapper.getPagedTranslate(listingIds, page
				* pageSize, pageSize);
	}

	public int getTranslateCount(List<String> listingIds) {
		return productbTranslateMapper.getTranslateCount(listingIds);
	}

	public List<ProductBaseTranslate> getTranslateByListingid(String clistingid) {
		return productbTranslateMapper.getTranslateByListingid(clistingid);
	}

	public List<ProductBaseTranslate> getTranslateByListingIds(
			List<String> list, Integer lang) {
		return productbTranslateMapper.getTranslateByListingIds(list, lang);
	}

	/**
	 * 只包含少量信息，自行查看sql再决定是否使用
	 * 
	 * @param list
	 * @param lang
	 * @return
	 */
	public List<ProductBaseTranslate> getTranslateLiteByListingIds(
			List<String> list, Integer lang) {
		return productbTranslateMapper.getTranslateLiteByListingIds(list, lang);
	}

	public List<ProductTranslate> getLanguageidByListingid(String clistingid) {
		return this.productTranslateEnquityDao.getLanguageIdByListingid(clistingid);
	}

	public List<ProductTranslate> getTranslatesByListingId(String clistingid) {
		return this.productTranslateEnquityDao.getProductTranslatesByListingid(clistingid);
	}

	public boolean updateTranslates(ProductTranslate translate) {
		int i = this.productTranslateUpdateDao.alterSelectiveProductTranslate(translate);
		if (1 == i) {
			return true;
		}
		return false;
	}

	public boolean insertTranslates(ProductTranslate translate) {
		int i = this.productTranslateUpdateDao.addSelectiveProductTranslate(translate);
		if (1 == i) {
			return true;
		}
		return false;
	}

	public boolean deleteTranslates(Integer id, String listingId) {
		int i = this.productTranslateUpdateDao.deleteByIdvalidListingId(id, listingId);
		if (1 == i) {
			return true;
		}
		return false;
	}

	public List<ProductTranslate> getTitleByClistingids(List<String> clistingids) {
		return this.productTranslateEnquityDao.getTitleByClistings(clistingids);
	}

	public List<ProductUrl> getUrlByClistingIds(List<String> clistingids) {
		return this.productUrlEnquityDao.getProductUrlByClistingids(clistingids);
	}
	
	public ProductTranslate getTranslateByListingidAndLanguage(String listingid,Integer language){
		return this.productTranslateEnquityDao.getTranslateByListingidAndLanguage(listingid, language);
	}

}
