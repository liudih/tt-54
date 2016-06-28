package services.category.recommend;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import javax.inject.Inject;

import mapper.base.VhostMapper;
import mapper.base.WebsiteMapper;
import mapper.product.CategoryWebsiteMapper;
import mapper.product.ProductBaseMapper;
import play.Logger;
import play.Play;
import play.libs.ws.WS;

import com.alibaba.fastjson.JSON;

import dao.manager.ICategoryProductRecommendDao;
import dto.ProductCategoryRecommend;
import dto.Vhost;
import dto.Website;
import dto.category.CategoryProductRecommend;
import dto.product.CategoryWebsiteWithName;
import forms.CategoryRecommendFrom;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

public class CategoryProductRecommendService {

	@Inject
	ICategoryProductRecommendDao cprDao;
	@Inject
	CategoryWebsiteMapper cpmapper;
	@Inject
	VhostMapper vhostMapper;
	@Inject
	WebsiteMapper websiteMapper;
	@Inject
	ProductBaseMapper productBaseMapper;

	private final static int showStatus = 1;
	private final static int deleteStatus = 4;

	public List<CategoryProductRecommend> select(Integer parentid,
			Integer categoryid, String sku, String createdate, Integer siteid,
			String device) {
		List<CategoryProductRecommend> cprList = cprDao
				.searchCategoryProductRecommend(showStatus, parentid,
						categoryid, sku, createdate, siteid, device);

		return cprList;
	}

	public List<CategoryProductRecommend> selectHistory(Integer parentid,
			Integer categoryid, String sku, String createdate, Integer siteid,
			String device) {
		List<CategoryProductRecommend> cprList = cprDao
				.searchCategoryProductRecommend(deleteStatus, parentid,
						categoryid, sku, createdate, siteid, device);

		return cprList;
	}

	public List<CategoryWebsiteWithName> getChildCategory(Integer categoryId,
			Integer language, Integer siteId) {
		CategoryWebsiteWithName cname = cpmapper.getCategory(categoryId,
				language, siteId);
		List<CategoryWebsiteWithName> pf = cpmapper.getChildCategories(
				cname.getIid(), language, siteId);
		if (pf == null) {
			return new ArrayList<CategoryWebsiteWithName>();
		}

		return pf;
	}

	public List<CategoryWebsiteWithName> getRootCategory(Integer languageid,
			Integer websiteid) {
		List<CategoryWebsiteWithName> pf = cpmapper.getRootCategories(
				languageid, websiteid);
		if (pf == null) {
			return new ArrayList<CategoryWebsiteWithName>();
		}

		return pf;
	}

	public List<Integer> insert(CategoryRecommendFrom crfrom, String createby) {
		int rec = -1;
		String category1 = crfrom.getCategory1();
		String category2 = crfrom.getCategory2();
		String category3 = crfrom.getCategory3();
		CategoryProductRecommend cpr = new CategoryProductRecommend();
		List<Integer> retint = new ArrayList<Integer>();
		String fullName = "";
		String[] cs1 = null;
		String[] cs2 = null;
		String[] cs3 = null;
		Integer level = 0;
		if (!"".equals(category1)) {
			cs1 = category1.split("\\,");
			cpr.setParentid(Integer.parseInt(cs1[0]));
			cpr.setCategoryid(Integer.parseInt(cs1[0]));
			cpr.setParentname(cs1[1]);
			cpr.setCategoryname(cs1[1]);
			retint.add(Integer.parseInt(cs1[0]));
			fullName += cs1[1];
			level = 1;
		}
		if (!"".equals(category2) && "".equals(category3)) {
			cs2 = category2.split("\\,");
			cpr.setCategoryid(Integer.parseInt(cs2[0]));
			cpr.setCategoryname(cs2[1]);
			retint.add(Integer.parseInt(cs2[0]));
			fullName += "," + cs2[1];
			level = 2;
		}
		if (!"".equals(category3)) {
			cs2 = category2.split("\\,");
			cs3 = category3.split("\\,");
			cpr.setCategoryid(Integer.parseInt(cs3[0]));
			retint.add(Integer.parseInt(cs3[0]));
			cpr.setCategoryname(cs3[1]);
			fullName += "," + cs2[1];
			fullName += "," + cs3[1];
			level = 3;
		}
		cpr.setCategoryfullname(fullName);
		cpr.setLevel(level);
		cpr.setSku(crfrom.getSku());
		cpr.setIwebsiteid(crfrom.getWebsiteid());
		cpr.setCdevice(crfrom.getDevice());
		cpr.setStatus(1);
		if ("true".equals(crfrom.getIsTop())) {
			cpr.setSequence(1);
			cprDao.updateSequence(cpr.getCategoryid(), 1, cpr.getIwebsiteid(),
					cpr.getCdevice());
		} else {
			Integer sequence = cprDao.searchMaxSequence(cpr.getCategoryid(),
					cpr.getIwebsiteid(), cpr.getCdevice());
			if (sequence == null) {
				cpr.setSequence(1);
			} else {
				cpr.setSequence(sequence + 1);
			}
		}
		cpr.setCreateby(createby);
		rec = cprDao.insertCategoryProductRecommend(cpr);
		if (rec < 1) {
			Logger.error("insertCategoryProductRecommend error");
		} else {
			if ("true".equals(crfrom.getIsTop())) {
				this.getRecommendsByCid(cpr.getCategoryid());
			} else {
				List<ProductCategoryRecommend> pcrlist = new ArrayList<ProductCategoryRecommend>();
				ProductCategoryRecommend recommend = new ProductCategoryRecommend();
				recommend.setCategoryId(cpr.getCategoryid());
				recommend.setWebsiteId(crfrom.getWebsiteid());
				recommend.setSku(cpr.getSku());
				recommend.setSort(cpr.getSequence());
				recommend.setLevel(level);
				resetSearch(cpr.getSku());
				pcrlist.add(recommend);
				this.recommendCommon(pcrlist);
			}

		}

		return retint;
	}

	public int deleteCategoryRecommend(Integer id, Integer categoryid,
			Integer sequence, String updateby, Integer siteid, String sdevice) {
		int rec = 0;
		rec = cprDao
				.updateDeleteSequence(categoryid, sequence, siteid, sdevice);
		if (rec < 1) {
			Logger.error("deleteCategoryRecommend updateSequence error");
		}
		rec = cprDao.updateStatus(id, deleteStatus, updateby);
		if (rec < 1) {
			Logger.error("deleteCategoryRecommend updateStatus error");
		} else {
			CategoryProductRecommend cpr = cprDao.getcprByIid(id);
			List<ProductCategoryRecommend> pcrlist = new ArrayList<ProductCategoryRecommend>();
			List<CategoryProductRecommend> categoryProductRecommends = cprDao
					.getCategoryRecommendsByCid(categoryid);
			if (null != categoryProductRecommends
					&& categoryProductRecommends.size() > 0) {
				for (CategoryProductRecommend recommend : categoryProductRecommends) {
					ProductCategoryRecommend pRecommend = new ProductCategoryRecommend();
					pRecommend.setCategoryId(recommend.getCategoryid());
					pRecommend.setLevel(recommend.getLevel());
					pRecommend.setSku(recommend.getSku());
					pRecommend.setWebsiteId(recommend.getIwebsiteid());
					pRecommend.setSort(recommend.getSequence());
					pcrlist.add(pRecommend);
				}
			}
			ProductCategoryRecommend recommend = new ProductCategoryRecommend();
			recommend.setCategoryId(categoryid);
			recommend.setWebsiteId(siteid);
			recommend.setSku(cpr.getSku());
			recommend.setSort(999);
			recommend.setLevel(cpr.getLevel());
			resetSearch(cpr.getSku());
			pcrlist.add(recommend);
			this.recommendCommon(pcrlist);
		}
		return rec;
	}

	public int onTopCategoryRecommend(Integer id, Integer categoryid,
			Integer sequence, String updateby, Integer siteid, String sdevice) {
		int rec = 0;
		rec = cprDao.updateSequenceTop(categoryid, sequence, siteid, sdevice);
		if (rec < 1) {
			Logger.error("onTopCategoryRecommend updateSequence error");
		}
		rec = cprDao.updateOnTop(id, updateby);
		if (rec < 1) {
			Logger.error("updateOnTop updateSequence error");
		} else {
			this.getRecommendsByCid(categoryid);
		}
		return rec;
	}

	public String checkProductCategoryBySku(String sku, Integer categoryid) {
		return cprDao.searchCategoryProductBySku(sku, categoryid);
	}

	public int checkCategoryRecommendBySku(String sku, Integer categoryid,
			Integer siteid, String device) {
		return cprDao.searchCategoryRecommendBySku(sku, categoryid, siteid,
				device);
	}

	public String getListingsBySku(String sku, Integer siteid) {
		return productBaseMapper.getListingIdBySkuAndWebsiteId(sku, siteid);
	}

	public List<Vhost> getDeviceAll() {
		return vhostMapper.getAllDevice();
	}

	public List<Website> getWebsiteAll() {
		return websiteMapper.getAll();
	}

	public CategoryProductRecommend selectById(Integer id) {
		return cprDao.searchCategoryRecommendById(id);
	}

	/**
	 * 对接搜索引擎(初始化数据)
	 * 
	 * @return
	 */
	public String getRecommends() {
		List<ProductCategoryRecommend> recommends = new ArrayList<ProductCategoryRecommend>();
		List<CategoryProductRecommend> categoryProductRecommends = cprDao
				.getCategoryRecommends();
		if (null != categoryProductRecommends
				&& categoryProductRecommends.size() > 0) {
			for (CategoryProductRecommend recommend : categoryProductRecommends) {
				ProductCategoryRecommend pRecommend = new ProductCategoryRecommend();
				pRecommend.setCategoryId(recommend.getCategoryid());
				pRecommend.setLevel(recommend.getLevel());
				pRecommend.setSku(recommend.getSku());
				pRecommend.setWebsiteId(recommend.getIwebsiteid());
				pRecommend.setSort(recommend.getSequence());
				recommends.add(pRecommend);
			}
		}

		return this.recommendCommon(recommends);
	}

	/**
	 * 根据品类id更新引擎
	 * 
	 * @param categoryId
	 * @return
	 */
	public String getRecommendsByCid(Integer categoryId) {
		List<ProductCategoryRecommend> recommends = new ArrayList<ProductCategoryRecommend>();
		List<CategoryProductRecommend> categoryProductRecommends = cprDao
				.getCategoryRecommendsByCid(categoryId);
		if (null != categoryProductRecommends
				&& categoryProductRecommends.size() > 0) {
			for (CategoryProductRecommend recommend : categoryProductRecommends) {
				ProductCategoryRecommend pRecommend = new ProductCategoryRecommend();
				pRecommend.setCategoryId(recommend.getCategoryid());
				pRecommend.setLevel(recommend.getLevel());
				pRecommend.setSku(recommend.getSku());
				pRecommend.setWebsiteId(recommend.getIwebsiteid());
				pRecommend.setSort(recommend.getSequence());
				resetSearch(recommend.getSku());
				recommends.add(pRecommend);
			}
		}

		return this.recommendCommon(recommends);
	}

	public String recommendCommon(List<ProductCategoryRecommend> recommends) {
		String json = JSON.toJSONString(recommends, true);
		String url = Play.application().configuration()
				.getString("search.cateogry.recommend");
		Logger.debug("---json--------:{}", json);
		String result = "";
		try {
			result = WS.client().url(url).post(json).map(resp -> {
				String payload = resp.getBody().toString();
				return payload;
			}).get(60000);

		} catch (Exception e) {
			// TODO: handle exception
			Logger.error("timeout,errormsg:" + e.getMessage());
		}
		return result;
	}
	
	public String resetSearch(String sku){
		String url = Play.application().configuration()
				.getString("product.recommend.sku");
		String response = "";
		NetHttpTransport transport = new NetHttpTransport();
		HttpRequestFactory httpRequestFactory = transport.createRequestFactory();
		try {
			GenericUrl surl = new GenericUrl(url+sku);
			response = httpRequestFactory.buildGetRequest(surl).execute().parseAsString();
			System.out.println("response-------------------------"+response);
		}
		catch (IOException e) {
			// TODO: handle exception
			Logger.error("timeout,errormsg:" + e.getMessage());
		}
		
		return response;
	}
}
