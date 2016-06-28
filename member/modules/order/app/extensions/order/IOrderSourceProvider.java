package extensions.order;

import play.mvc.Http.Context;

/**
 * 用于订单来源判断
 * 
 * @author kmtong
 *
 */
public interface IOrderSourceProvider {

	String getSource(Context current);

}
