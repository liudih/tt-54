package dto.mobile;

import java.util.List;

public class CategoryCompositeInfo {

	private CategoryInfo self;

	private List<CategoryCompositeInfo> children;

	public CategoryCompositeInfo(CategoryInfo self,
			List<CategoryCompositeInfo> children) {
		super();
		this.self = self;
		this.children = children;
	}

	public CategoryInfo getSelf() {
		return self;
	}

	public void setSelf(CategoryInfo self) {
		this.self = self;
	}

	public List<CategoryCompositeInfo> getChildren() {
		return children;
	}

	public void setChildren(List<CategoryCompositeInfo> children) {
		this.children = children;
	}
}
