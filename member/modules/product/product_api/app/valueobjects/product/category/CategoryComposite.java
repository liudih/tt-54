package valueobjects.product.category;

import java.io.Serializable;
import java.util.List;

import dto.product.CategoryWebsiteWithName;

public class CategoryComposite implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final CategoryWebsiteWithName self;
	final List<CategoryComposite> children;

	public CategoryComposite(CategoryWebsiteWithName self,
			List<CategoryComposite> children) {
		super();
		this.self = self;
		this.children = children;
	}

	public CategoryWebsiteWithName getSelf() {
		return self;
	}

	public List<CategoryComposite> getChildren() {
		return children;
	}

}
