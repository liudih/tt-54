package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.ShippingMethodDetail;
import dto.shipping.ShippingMethod;

public interface ShippingMethodMapper {

	List<ShippingMethodDetail> getShippingMethods(
			@Param("storageId") Integer storageId,
			@Param("country") String country, @Param("weight") Double weight,
			@Param("lang") Integer lang, @Param("price") Double price,
			@Param("isSpecial") Boolean isSpecial);

	@Select("select * from t_shipping_method where iid = #{id}")
	ShippingMethod getShippingMethodById(Integer id);

	@Select("select m.*,d.cname, d.ctitle, d.ccontent, d.cfreecontent, d.ilanguageid , d.cimageurl, d.igroupid from t_shipping_method m "
			+ "inner join t_shipping_method_detail d on d.ccode = m.ccode where m.iid = #{0} and d.ilanguageid = #{1} limit 1")
	ShippingMethodDetail getShippingMethodDetail(Integer id, Integer lang);

	@Select("select * from t_shipping_method_detail where ccode = #{0} and ilanguageid = #{1} limit 1")
	ShippingMethodDetail getShippingMethodDetailByCode(String code, Integer lang);

	@Select("select ctitle from t_shipping_method_detail where ccode = #{0} and ilanguageid = #{1}")
	String getTitleById(String code, Integer lang);

	@Insert("insert into t_shipping_method (istorageid, benabled,bexistfree,ffreebeginprice,ffreeendprice,"
			+ " ccountrys, crule, csuperrule "
			+ ", fbeginprice, fendprice, bistracking, bisspecial"
			+ ", ccode,istartweight,iendweight) "
			+ "values (#{istorageid}, #{benabled}, #{bexistfree}, #{ffreebeginprice}, #{ffreeendprice},#{ccountrys}, #{crule}, #{csuperrule}"
			+ ", #{fbeginprice}, #{fendprice},#{bistracking}, #{bisspecial}"
			+ ",#{ccode}, #{istartweight}, #{iendweight})")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insertBase(ShippingMethod method);

	@Insert("insert into t_shipping_method_detail (ilanguageid, cname, ctitle, ccontent, cfreecontent,ccode,cimageurl)"
			+ " values (#{ilanguageid}, #{cname}, #{ctitle}, #{ccontent}, #{cfreecontent},#{ccde},#{cimageurl})")
	int insertDetail(ShippingMethodDetail method);

	@Select("select * from t_shipping_method_detail where ilanguageid = #{0}")
	List<ShippingMethodDetail> getShippingMethodDetailByLanguageId(
			Integer ilanguageid);

	@Update("<script>update t_shipping_method <set>"
			+ "<if test=\"istorageid != null\">istorageid = #{istorageid}, </if>"
			+ "<if test=\"benabled != null\">benabled = #{benabled}, </if>"
			+ "<if test=\"bexistfree != null\">bexistfree = #{bexistfree}, </if>"
			+ "<if test=\"ffreebeginprice != null\">ffreebeginprice = #{ffreebeginprice} </if>"
			+ "<if test=\"ffreeendprice != null\">ffreeendprice = #{ffreeendprice} </if>"
			+ "<if test=\"ccountrys != null\">ccountrys = #{ccountrys} </if>"
			+ "<if test=\"crule != null\">crule = #{crule} </if>"
			+ "<if test=\"csuperrule != null\">csuperrule = #{csuperrule} </if>"
			+ "<if test=\"fbeginprice != null\">fbeginprice = #{fbeginprice} </if>"
			+ "<if test=\"fendprice != null\">fendprice = #{fendprice} </if>"
			+ "<if test=\"bistracking != null\">bistracking = #{bistracking} </if>"
			+ "<if test=\"bisspecial != null\">bisspecial = #{bisspecial} </if>"
			+ "<if test=\"ccode != null\">ccode = #{ccode} </if>"
			+ "<if test=\"istartweight != null\">istartweight = #{istartweight} </if>"
			+ "<if test=\"iendweight != null\">iendweight = #{iendweight} </if>"
			+ "</set> where iid = #{iid}</script>")
	int updateRule(ShippingMethod rule);

	@Select("select count(ccode) from t_shipping_method where ccode=#{0} and istorageid=#{1} and ccountrys=#{2} and crule=#{3} and csuperrule=#{4} and istartweight=#{5} and iendweight=#{6} ")
	Integer getShippingMethodCount(String code, Integer storageid,
			String countrys, String rule, String superrule,
			Integer startweight, Integer endweight);

	@Update("update t_shipping_method set benabled = #{1} where ccode = #{0}")
	Integer updateEnableByCode(String ccode, boolean benabled);

	@Select("<script>"
			+ "select * from t_shipping_method where iid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach>"
			+ "</script>")
	List<ShippingMethod> getShippingMethodsByIds(@Param("list")List<Integer> smIds);
}
