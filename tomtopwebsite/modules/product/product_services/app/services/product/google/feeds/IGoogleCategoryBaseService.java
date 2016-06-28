package services.product.google.feeds;

import java.util.List;

import valueobjects.base.Page;
import dto.product.google.category.GoogleCategory;
import dto.product.google.category.MerchantsProductDto;
import dto.product.google.category.SearchMerchantsProductDto;

public interface IGoogleCategoryBaseService {

	int saveOrUpdate(GoogleCategory googleCategory);

	abstract List<GoogleCategory> getFirstCategory();

	abstract List<GoogleCategory> getChildsByParentId(int cid);

	abstract String getCpathByCid(int cid);

	abstract List<GoogleCategory> getDetailByCid(int cid);

	abstract GoogleCategory getCategoryByCid(int cid);

	Page<GoogleCategory> getAll(int p, int i, GoogleCategory googleBase);

	dto.product.google.category.GoogleCategory getCategoryByIid(Integer id);
	
}
