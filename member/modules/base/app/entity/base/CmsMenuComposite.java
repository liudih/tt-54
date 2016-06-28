package entity.base;

import java.util.List;


public class CmsMenuComposite {
	final CmsMenu self;
	final List<CmsMenuComposite> children;

	public CmsMenuComposite(CmsMenu self,
			List<CmsMenuComposite> children) {
		super();
		this.self = self;
		this.children = children;
	}

	public CmsMenu getSelf() {
		return self;
	}

	public List<CmsMenuComposite> getChildren() {
		return children;
	}

}
