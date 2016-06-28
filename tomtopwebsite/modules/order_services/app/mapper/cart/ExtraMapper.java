package mapper.cart;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import valueobjects.order_api.cart.ExtraLine;

public interface ExtraMapper {

	@Select("SELECT cpluginid pluginId, cpayload payload FROM t_cart_extra WHERE ccartbaseid = #{0}")
	List<ExtraLine> getByCartId(String cartId);

	@Insert("INSERT INTO t_cart_extra (cid, ccartbaseid, cpluginid, cpayload, ccreateuser) "
			+ "VALUES (#{cid}, #{ccartbaseid}, #{cpluginid}, #{cpayload}, #{ccreateuser})")
	@Options(useGeneratedKeys = true, keyProperty = "cid", keyColumn = "cid")
	int insert(dto.cart.ExtraLine extraLine);

	@Delete("DELETE FROM t_cart_extra WHERE ccartbaseid = #{0} AND cpluginid = #{1}")
	int deleteByCartIdAndPluginId(String cartId, String pluginId);

	@Delete("DELETE FROM t_cart_extra WHERE ccartbaseid = #{0}")
	int deleteByCartId(String cartId);

	@Select("SELECT COUNT(cpluginid) FROM t_cart_extra WHERE ccartbaseid = #{0} AND cpluginid = #{1}")
	int validExtraLine(String CartId, String extraId);
}
