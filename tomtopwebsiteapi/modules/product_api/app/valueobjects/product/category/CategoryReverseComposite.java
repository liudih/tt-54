package valueobjects.product.category;

import java.io.Serializable;
import java.util.List;

import valueobjects.product.IProductFragment;
import dto.product.CategoryWebsiteWithName;

public class CategoryReverseComposite implements IProductFragment, Serializable {

	private static final long serialVersionUID = 1L;

	final CategoryWebsiteWithName self;
	final CategoryReverseComposite parent;
	final List<CategoryComposite> children;

	public CategoryReverseComposite(CategoryWebsiteWithName self,
			CategoryReverseComposite parent, List<CategoryComposite> children) {
		super();
		this.self = self;
		this.parent = parent;
		this.children = children;
	}

	public CategoryWebsiteWithName getSelf() {
		return self;
	}

	public CategoryReverseComposite getParent() {
		return parent;
	}

	public List<CategoryComposite> getChildren() {
		return children;
	}

}
