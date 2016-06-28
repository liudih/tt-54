package dao.product.impl;

import java.util.List;

import mapper.google.category.GoogleCategoryBaseMapper;

import com.google.inject.Inject;

import dao.product.IGoogleCategoryBaseDao;
import dto.product.google.category.GoogleCategory;
import dto.product.google.category.SearchGoogleCategory;

public class GoogleCategoryBaseDao implements IGoogleCategoryBaseDao {

	@Inject
	GoogleCategoryBaseMapper googleCategoryBaseMapper;

	@Override
	public List<GoogleCategory> getFirstCategory() {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.getFirstCategory();
	}

	@Override
	public List<GoogleCategory> getChildsByParentId(int cid) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.getChildsByParentId(cid);
	}

	@Override
	public String getCpathByCid(int cid) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.getCpathByCid(cid);
	}

	@Override
	public List<GoogleCategory> getDetailByCid(int cid) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.getDetailByCid(cid);
	}

	@Override
	public GoogleCategory getCategoryByCid(int cid) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.getCategoryByCid(cid);
	}

	@Override
	public int insert(GoogleCategory googleCategory) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.insert(googleCategory);
	}

	@Override
	public int updateUsingCategoryId(int icategory, String cpath, int parentid) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.updateUsingCategoryId(icategory, cpath,
				parentid);
	}

	@Override
	public GoogleCategory getIdByCpath(String cpath) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.getIdByCpath(cpath);
	}

	@Override
	public int updateUsingCpath(int icategory, String cpath, int parentid,
			String cname) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.updateUsingCpath(icategory, cpath,
				parentid, cname);
	}

	@Override
	public List<GoogleCategory> getAll(int p, int i, String cpath) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.getAll(p, i, cpath);
	}

	@Override
	public int getCount(String cpath) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.getCount(cpath);
	}

	@Override
	public GoogleCategory getCategoryByIid(Integer id) {
		// TODO Auto-generated method stub
		return googleCategoryBaseMapper.getCategoryByIid(id);
	}
	@Override
	public List<SearchGoogleCategory> autoMerchantGoogleCategoryProduct(int page,  int pageSize) {
		return googleCategoryBaseMapper.autoMerchantGoogleCategoryProduct(page, pageSize);
	}

}
