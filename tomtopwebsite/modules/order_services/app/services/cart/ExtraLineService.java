package services.cart;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.cart.ExtraMapper;

import org.elasticsearch.common.collect.Maps;

import play.Logger;
import services.common.UUIDGenerator;
import services.member.login.LoginService;
import valueobjects.order_api.cart.ExtraLine;

/**
 *
 * @ClassName: ExtraLineService
 * @Description: 一些有关ExtraLine的对DB的操作
 * @author luojiaheng
 * @date 2015年1月14日 下午3:18:48
 *
 */
public class ExtraLineService implements IExtraLineService {

	@Inject
	ExtraMapper extraMapper;

	@Inject
	LoginService service;

	/* (non-Javadoc)
	 * @see services.cart.IExtraLineService#addExtraLine(java.lang.String, valueobjects.order_api.cart.ExtraLine)
	 */
	@Override
	public String addExtraLine(String cartId, ExtraLine line) {
		dto.cart.ExtraLine entity = new dto.cart.ExtraLine(
				UUIDGenerator.createAsString(), cartId, line.getPluginId(),
				line.getPayload());
		int i = extraMapper.insert(entity);
		if (1 == i) {
			return entity.getCid();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see services.cart.IExtraLineService#getExtraLinesByCartId(java.lang.String)
	 */
	@Override
	public Map<String, ExtraLine> getExtraLinesByCartId(String cartId) {
		List<ExtraLine> lines = extraMapper.getByCartId(cartId);
		Logger.debug("Extra Lines: {}", lines);
		return Maps.newHashMap(Maps.uniqueIndex(lines, e -> e.getPluginId()));
	}

	/* (non-Javadoc)
	 * @see services.cart.IExtraLineService#deleteByCartIdAndPluginId(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean deleteByCartIdAndPluginId(String cartId,
			String pluginId) {
		int i = extraMapper.deleteByCartIdAndPluginId(cartId, pluginId);
		if (1 == i) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see services.cart.IExtraLineService#deleteExtraLineByCartId(java.lang.String)
	 */
	@Override
	public boolean deleteExtraLineByCartId(String cartId) {
		int i = extraMapper.deleteByCartId(cartId);
		if (i > 0) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see services.cart.IExtraLineService#validExtraLine(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean validExtraLine(String cartId, String extraId) {
		int i = extraMapper.validExtraLine(cartId, extraId);
		if (0 < i) {
			return false;
		}
		return true;
	}
}
