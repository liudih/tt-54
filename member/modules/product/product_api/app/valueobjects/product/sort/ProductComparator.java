package valueobjects.product.sort;

import java.util.Comparator;

public class ProductComparator implements Comparator<ProductSort> {

	@Override
	public int compare(ProductSort productSort1, ProductSort productSort2) {
		int flag = productSort1.getSaleCount().compareTo(
				productSort2.getSaleCount());
		if (flag == 0) {
			Integer commentCount1 = productSort1.getCommentCount();
			Integer commentCount2 = productSort2.getCommentCount();
			if (commentCount1 > commentCount2) {
				return -1;
			} else if (commentCount1 == commentCount2) { 
				Integer viewCount1 = productSort1.getViewCount();
				Integer viewCount2 = productSort2.getViewCount();
				if (viewCount1 > viewCount2) {
					return -1;
				} else if (viewCount1 == viewCount2) { 
					return 0;
	            } else {  
	                return 1;  
	            }
            } else {  
                return 1;  
            }
		} else {
			return flag;
		}
	}

}
