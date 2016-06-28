package mapper.loyalty;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.Seo;
import entity.loyalty.CouponCode;
import forms.loyalty.CouponCodeForm;

public interface CouponCodeMapper {

	List<CouponCode> list(Map<Object, Object> paras);

	Integer getTotal(Map<Object, Object> paras);

	int add(CouponCode couponCode);

	int del(List<Integer> list);

	@Select("select icouponruleid from t_coupon_code where ccode = #{0}")
	Integer getRuleIdByCode(String code);

	@Select("select * from t_coupon_code where iid = #{0}")
	CouponCode getCouponCodeByCodeId(Integer codeId);

	@Select("select * from t_coupon_code where ccode = #{0}")
	CouponCode getCouponCodeByCode(String code);

	@Select("select ccode from t_coupon_code where iid = #{0}")
	String getCodeById(Integer id);
	
	@Select("SELECT * FROM t_coupon_code")
	List<CouponCode> getCouponCodeList();

	@Select("<script>"
			+ "select COUNT(iid) from t_coupon_code where 1=1 "
			+ "<if test=\"icouponruleid != null  \">and icouponruleid=#{icouponruleid} </if>"
			+ "<if test=\"ccode !=null \">and ccode=#{ccode} </if>"
			+ "</script>")
	Integer getCount(@Param("icouponruleid") Integer icouponruleid,@Param("ccode") String ccode);

	
	@Select("<script>"
			+ "select * from t_coupon_code where 1=1 "
			+ "<if test=\"icouponruleid != null \">and icouponruleid=#{icouponruleid} </if>"
			+ "<if test=\"ccode !=null \">and ccode=#{ccode} </if> ORDER BY iid desc limit #{pageSize} offset #{pageSize} * (#{pageNum} - 1)"
			+ "</script>")
	List<CouponCode> getCouponCodes(@Param("icouponruleid") Integer icouponruleid,
			@Param("ccode") String ccode, @Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

}
