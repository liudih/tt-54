package valueobjects.interaction;

import java.util.List;

import dto.ProductPost;
import valueobjects.product.IProductFragment;

public class ProductPostList implements IProductFragment {
	private Integer count;
	private List<ProductPost> list;

	public ProductPostList() {
	}

	public ProductPostList(Integer count, List<ProductPost> list) {
		super();
		this.count = count;
		this.list = list;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<ProductPost> getList() {
		return list;
	}

	public void setList(List<ProductPost> list) {
		this.list = list;
	}

}
