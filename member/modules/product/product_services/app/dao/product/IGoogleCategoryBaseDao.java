package dao.product;

import java.util.List;

import dto.product.google.category.GoogleCategory;
import dto.product.google.category.SearchGoogleCategory;

public interface IGoogleCategoryBaseDao {

	public abstract List<GoogleCategory> getFirstCategory();

	public abstract List<GoogleCategory> getChildsByParentId(int cid);

	public abstract String getCpathByCid(int cid);

	public abstract List<GoogleCategory> getDetailByCid(int cid);

	public abstract GoogleCategory getCategoryByCid(int cid);

	public abstract int insert(GoogleCategory googleCategory);

	public abstract int updateUsingCategoryId(int icategory, String cpath,
			int parentid);

	GoogleCategory getIdByCpath(String cpath);

	int updateUsingCpath(int icategory, String cpath, int parentid, String cname);

	public abstract List<GoogleCategory> getAll(int p, int i,
			String cpath);

	public abstract int getCount(String cpath);

	public abstract GoogleCategory getCategoryByIid(Integer id);
	
	List<SearchGoogleCategory> autoMerchantGoogleCategoryProduct(int page,
			int pageSize);

}