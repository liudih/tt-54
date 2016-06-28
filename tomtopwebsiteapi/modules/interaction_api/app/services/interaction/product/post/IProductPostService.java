package services.interaction.product.post;

import java.util.List;
import java.util.Map;

import valueobjects.base.Page;
import context.WebContext;
import dto.interaction.ProductPost;

public interface IProductPostService {

	boolean addProductPost(ProductPost faq);

	Page<dto.ProductPost> getByListingidPage(String listingid, int typeid,
			int page, int pageSize);

	Page<Map<String, List<ProductPost>>> getProductPostList(String listingId,
			WebContext context, Integer itypeid, Integer state, Integer page,
			Integer pageSize);

	/**
	 * 分页获取ProductPost信息
	 * 
	 * @param context
	 * @param itypeid
	 * @param state
	 * @param page
	 * @param pageSize
	 * @author lij
	 * @return
	 */
	List<ProductPost> getProductPostList(WebContext context, Integer itypeid,
			Integer state, Integer page, Integer pageSize);

	/**
	 * 通过关键字搜索信息
	 * 
	 * @param context
	 * @param key
	 * @param itypeid
	 * @param state
	 * @param page
	 * @param pageSize
	 * @author lij
	 * @return
	 */
	List<ProductPost> getProductPostListBySearch(WebContext context,
			String key, Integer itypeid, Integer state, Integer page,
			Integer pageSize);
	
	/**
	 * 通过主键获取ProductPost信息
	 * @param iid
	 * @return
	 */
	ProductPost getProductPostByIid(Integer iid);
}