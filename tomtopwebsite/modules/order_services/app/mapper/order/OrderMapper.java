package mapper.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import valueobjects.order_api.DropShipOrderMessage;
import valueobjects.order_api.OrderCommission;
import dto.api.OrderApiVo;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.OrderIdStatusId;
import dto.order.OrderReportForm;
import dto.order.OrderWithDtail;

public interface OrderMapper {

	@Select("select iid,cemail,iwebsiteid,ccountrysn,ccountry,cstreetaddress,ccity,cprovince,cpostalcode,"
			+ "ctelephone,cfirstname,cmiddlename,clastname,istorageid,ishippingmethodid,fshippingprice,"
			+ "fordersubtotal,fextra,fgrandtotal,ccartid,cpaymentid,istatus,ccurrency,corigin,dcreatedate,"
			+ "dpaymentdate,cmemberemail,cmessage,ishow,cip,cremark,creceiveraccount,cshippingcode,cordernumber"
			+ " from t_order where iid=#{0}")
	Order getOrderByOrderId(Integer iorderid);

	@Select({ "<script> select iid from t_order where iid in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	List<Integer> getExistsIds(List<Integer> orderids);

	@Insert("INSERT INTO t_order (cemail, ccountrysn, ccountry, cstreetaddress, ccity, cprovince, cpostalcode, ctelephone, "
			+ "istorageid, ishippingmethodid, fshippingprice, fordersubtotal, fextra, fgrandtotal, ccartid, istatus, "
			+ "ccurrency, cfirstname, cmiddlename, clastname, cpaymentid, iwebsiteid, corigin, cmemberemail, cmessage, "
			+ "cip, cshippingcode, cvhost, cordernumber, cpaymenttype) "
			+ "VALUES (#{cemail}, #{ccountrysn}, #{ccountry},  #{cstreetaddress}, #{ccity},  #{cprovince}, #{cpostalcode}, "
			+ "#{ctelephone}, #{istorageid}, #{ishippingmethodid}, #{fshippingprice}, #{fordersubtotal}, "
			+ "#{fextra}, #{fgrandtotal}, #{ccartid}, #{istatus}, #{ccurrency}, #{cfirstname}, #{cmiddlename}, #{clastname}, "
			+ "#{cpaymentid}, #{iwebsiteid}, #{corigin}, #{cmemberemail}, #{cmessage}, #{cip}, #{cshippingcode}, #{cvhost}, "
			+ "#{cordernumber}, #{cpaymenttype})")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(Order order);

	@Select("SELECT COUNT(iid) FROM t_order WHERE ccartid = #{0} AND istatus IS NOT NULL")
	int checkOrder(String cartId);

	@Select("select count(iid) from t_order where cmemberemail = #{0} and iwebsiteid = #{1} and istatus IS NOT NULL")
	Integer getCountByEmail(String email, Integer siteId);

	@Select("<script>select count(1) from t_order o "
			+ "where o.cmemberemail = #{email} and o.iwebsiteid = #{siteId} "
			+ "<if test=\"status != null\">and o.istatus = #{status} </if>"
			+ "<if test=\"isShow != null\">and o.ishow = #{isShow} </if>"
			+ "<if test=\"isNormal == true\">and not exists (select iorderid from t_order_tag where o.iid = iorderid)</if>"
			+ "</script>")
	Integer getCountByEmailAndStatus(@Param("email") String email,
			@Param("status") Integer status, @Param("siteId") Integer siteId,
			@Param("isShow") Integer isShow, @Param("isNormal") boolean isNormal);

	@Select("<script>select count(1) from t_order o "
			+ "<if test=\"tag != null\">inner join t_order_tag t on o.iid = t.iorderid </if>"
			+ "where o.cmemberemail = #{email} and o.iwebsiteid = #{siteId} "
			+ "<if test=\"status != null\">and o.istatus = #{status} </if>"
			+ "<if test=\"isShow != null\">and o.ishow = #{isShow} </if>"
			+ "<if test=\"tag != null\">and t.ctag = #{tag} </if>"
			+ "</script>")
	Integer countByEmailAndStatusAndTag(@Param("email") String email,
			@Param("status") Integer status, @Param("siteId") Integer siteId,
			@Param("isShow") Integer isShow, @Param("tag") String tag);
	
	//modify by lijun
//	@Update("<script>update t_order set istatus = #{1} where iid = #{0} <if test=\"email != null\">"
//			+ "and cmemberemail = #{email} </if><if test=\"srcStatus != null\">and istatus = #{srcStatus} </if></script>")
	int updateStatus(@Param("orderId") Integer orderId,@Param("status") Integer status,
			@Param("email") String email, @Param("srcStatus") Integer srcStatus);

	List<Order> searchOrders(@Param("email") String email,
			@Param("status") Integer status, @Param("start") Date start,
			@Param("end") Date end, @Param("name") String name,
			@Param("siteId") Integer siteId,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum, @Param("show") Integer show,
			@Param("orderNumber") String orderNumber,
			@Param("transactionId") String transactionId,
			@Param("firstName") String firstName,@Param("isNormal") boolean isNormal);

	List<Order> searchOrdersByTag(@Param("email") String email,
			@Param("status") Integer status, @Param("start") Date start,
			@Param("end") Date end, @Param("name") String name,
			@Param("siteId") Integer siteId,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum, @Param("show") Integer show,
			@Param("orderNumber") String orderNumber,
			@Param("transactionId") String transactionId,
			@Param("firstName") String firstName, @Param("tag") String tag);

	Integer searchOrderCount(@Param("email") String email,
			@Param("status") Integer status, @Param("start") Date start,
			@Param("end") Date end, @Param("name") String name,
			@Param("siteId") Integer siteId, @Param("show") Integer show,
			@Param("paymentId") String paymentId,
			@Param("transactionId") String transactionId,
			@Param("orderNumber") String orderNumber,
			@Param("firstName") String firstName,
			@Param("isNormal") boolean isNormal);

	Integer searchOrderCountByTag(@Param("email") String email,
			@Param("status") Integer status, @Param("start") Date start,
			@Param("end") Date end, @Param("name") String name,
			@Param("siteId") Integer siteId, @Param("show") Integer show,
			@Param("paymentId") String paymentId,
			@Param("transactionId") String transactionId,
			@Param("orderNumber") String orderNumber,
			@Param("firstName") String firstName, @Param("tag") String tag);

	boolean updateShow(@Param("type") Integer type,
			@Param("ids") List<Integer> ids, @Param("email") String email);

	List<Order> searchOrdersFullMessage(@Param("status") List<Integer> list,
			@Param("start") Date start, @Param("end") Date end,
			@Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost, @Param("code") String code);

	List<Order> searchOrdersFullMessageByExcludeUser(
			@Param("status") List<Integer> list, @Param("start") Date start,
			@Param("end") Date end, @Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId, @Param("code") String code,
			@Param("vhost") String vhost, @Param("excUser") List<String> excUser);

	List<Order> searchOrdersTransaction(@Param("status") Integer status,
			@Param("start") Date start, @Param("end") Date end,
			@Param("siteId") Integer siteId,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost);

	Integer searchOrderMessageCount(@Param("email") String email,
			@Param("status") Integer status, @Param("start") Date start,
			@Param("end") Date end, @Param("siteId") Integer siteId,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost);

	int updatePaymentAndMessage(@Param("id") Integer orderId,
			@Param("status") Integer status, @Param("email") String email,
			@Param("paymentId") String paymentId,
			@Param("message") String message);

	@Insert("INSERT INTO t_order (iid,cemail, ccountrysn, ccountry, cstreetaddress, ccity, cprovince, cpostalcode, ctelephone, "
			+ "istorageid, ishippingmethodid, fshippingprice, fordersubtotal, fextra, fgrandtotal, ccartid, istatus, "
			+ "ccurrency, cfirstname, cmiddlename, clastname, cpaymentid, iwebsiteid, corigin, cmemberemail, cmessage, cip, cordernumber ) "
			+ "VALUES (#{iid},#{cemail}, #{ccountrysn}, #{ccountry},  #{cstreetaddress}, #{ccity},  #{cprovince}, #{cpostalcode}, "
			+ "#{ctelephone}, #{istorageid}, #{ishippingmethodid}, #{fshippingprice}, #{fordersubtotal}, "
			+ "#{fextra}, #{fgrandtotal}, #{ccartid}, #{istatus}, #{ccurrency}, #{cfirstname}, #{cmiddlename}, #{clastname}, "
			+ "#{cpaymentid}, #{iwebsiteid}, #{corigin}, #{cmemberemail}, #{cmessage}, #{cip}, #{cordernumber})")
	int insertContainId(Order order);

	int insertBatch(List<Order> order);

	@Delete({ "<script> delete from t_order where iid  in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	int deleteById(List<Integer> orderid);

	@Update("update t_order set ctransactionid = #{1} where iid = #{0}")
	int updateTransactionId(Integer orderId, String transactionId);

	@Update("update t_order set creceiveraccount = #{1} where iid = #{0}")
	int updatePaymentAccount(Integer orderId, String account);

	@Select("SELECT COUNT(iid) FROM t_order WHERE iid = #{0} and istatus = #{1}")
	int checkOrderStatus(Integer orderId, Integer status);

	@Update("update t_order set fgrandtotal = #{1} where iid = #{0}")
	int updateOrderGrandtotal(Integer orderId, Double grandtotal);

	@Update("update t_order set cremark = #{1} where iid = #{0}")
	int updateOrderRemark(Integer orderId, String remark);

	@Update("update t_order set istatus = #{1} where iid = #{0}")
	int updateOrderStatus(Integer orderId, Integer status);

	@Select("<script> select count(iid) from t_order where "
			+ "dcreatedate between #{start} and #{end} and "
			+ "istatus in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>")
	int getOrderCountByDateAndStatus(@Param("start") Date start,
			@Param("end") Date end, @Param("list") List<Integer> status);

	/*
	 * @Select("<script>select * from t_order " + "where istatus is not null " +
	 * "<if test=\"status != null\">and istatus = #{status} </if>" +
	 * "<if test=\"start != null\">and dcreatedate &gt;= #{start} </if>" +
	 * "<if test=\"end != null\">and dcreatedate &lt;= #{end} </if>" +
	 * "<if test=\"orderNum != null\">and cordernumber = #{orderNum} </if>" +
	 * "<if test=\"paymentId != null\">and cpaymentid = #{paymentId} </if>" +
	 * "<if test=\"email != null\">and cemail = #{email} </if>" +
	 * "<if test=\"transactionId != null\">and ctransactionid = #{transactionId} </if>"
	 * + "<if test=\"cvhost != null\">and cvhost = #{cvhost} </if>" +
	 * "order by dcreatedate</script>")
	 */
	List<Order> getOrders(@Param("status") List<Integer> list,
			@Param("start") Date start, @Param("end") Date end,
			@Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost, @Param("code") String code);

	@Select("SELECT istatus FROM t_order WHERE iid = #{0}")
	Integer getOrderStatusByOrderId(Integer orderId);

	@Select("SELECT iid FROM t_order WHERE cordernumber = #{0}")
	Integer getOrderIdByOrderNumber(String orderNumber);

	@Select("SELECT iid as iorderid, istatus, cordernumber FROM t_order WHERE cordernumber = #{0}")
	OrderIdStatusId getOrderIdStatusByOrderNumber(String orderNumber);

	@Update("update t_order set dpaymentdate = now() where iid = #{0}")
	int updatePaymentTime(int orderID);
	
	@Update("update t_order set dpaymentdate = now() where cordernumber = #{0}")
	int updatePaymentTimeByOrderNum(String orderNum);

	@Select("select * from t_order where (corigin=#{0} or corigin like #{0}||':%') and istatus=#{1}")
	List<Order> getOrderForCommission(String aid, int status);

	@Select({"<script>",
		"select * from t_order where istatus=#{status}",
		"and corigin in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
		"</script>"})
	List<Order> getOrderForCommissionByAids(@Param("list") List<String> aids, @Param("status") Integer status);
	
	@Select({
			"<script>",
			"select t.*,d.csku from t_order t ",
			"inner join t_order_detail d on t.iid=d.iorderid where 1=1 ",
			"<if test=\"list!=null and list.size()>0\">",
			"and t.iid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</if>",
			"<if test=\"sn!=null and sn!=''\">",
			"and (d.csku like '%'||trim(#{sn})||'%' or t.cordernumber like '%'||trim(#{sn})||'%') ",
			"</if>", "</script>" })
	List<OrderCommission> getOrderCommissions(@Param("list") List<Integer> ids,
			@Param("sn") String searchname);

	@Select("select * from t_order where cordernumber = #{0} limit 1")
	Order getOrderByOrderNumber(String orderNumber);

	@Select({
			"<script>",
			"select * from t_order where corigin is not null and corigin!='' and istatus in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"<if test=\"sd!=null\">", "and dcreatedate &gt;= #{sd} ", "</if>",
			"<if test=\"ed!=null\">", "and dcreatedate &lt;= #{ed} ", "</if>",
			"<if test=\"website!=null and website!=0\">", "and iwebsiteid = #{website} ", "</if>",
			"order by dcreatedate asc ", "</script>" })
	public List<Order> getOrderByAidAndDate(@Param("sd") Date start,
			@Param("ed") Date end, @Param("list") List<Integer> statusIds, @Param("website")Integer website);

	@Select("select iid,cemail,iwebsiteid,ccountrysn,ccountry,cstreetaddress,ccity,cprovince,cpostalcode,"
			+ "ctelephone,cfirstname,cmiddlename,clastname,istorageid,ishippingmethodid,fshippingprice,"
			+ "fordersubtotal,fextra,fgrandtotal,ccartid,cpaymentid,istatus,ccurrency,corigin,dcreatedate,"
			+ "dpaymentdate,cmemberemail,cmessage,ishow,cip,cremark,creceiveraccount,cshippingcode ,dcreatedate,cordernumber from t_order where split_part(corigin, ':', 1)=#{0}"
			+ " and to_date(to_char(dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between  #{1} and #{2} order by dcreatedate ")
	List<Order> getOrdersByDateRange(String origin, Date begindate, Date enddate);

	@Select("<script>select o.*,d.*,s.cname as statusname from t_order o "
			+ " inner join t_order_detail d on o.iid=d.iorderid  "
			+ " inner join t_order_status s on o.istatus=s.iid "
			+ "where 1=1 "
			+ "<if test=\"list != null\"> and split_part(o.corigin, ':', 1) in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\"  separator=\",\" close=\")\">#{item}</foreach></if>"
			+ "<if test=\"skuornum != null\">and (d.csku=#{skuornum} or o.cordernumber=#{skuornum} ) </if>"
			+ "<if test=\"status != null\"> and  s.iid=#{status}</if>"
			+ "<if test=\"orderStatus != null\">and o.istatus=#{orderStatus} </if>"
			+ "<if test=\"website != null and website != 0\">and o.iwebsiteid=#{website} </if>"
			+ "<if test=\"begindate != null and enddate != null \"> and to_date(to_char(o.dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between #{begindate} and #{enddate} </if>"
			+ " order by o.dcreatedate " + "</script>")
	List<OrderWithDtail> getOrdersWithDetail(Map<String, Object> queryParamMap);

	@Select("<script>select o.*,d.* from t_order o "
			+ " inner join t_order_detail d on o.iid=d.iorderid where 1=1   "
			+ "<if test=\"list != null\"> and  iid in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach></if>"
			+ " limit #{pageSize} offset (#{page}-1)*#{pageSize}" + "</script>")
	List<OrderWithDtail> getOrdersWithDetailByIds(
			@Param("list") List<Integer> ids, @Param("page") int page,
			@Param("pageSize") int pageSize);

	@Select("<script>select o.*,d.*,s.cname as statusname from t_order o "
			+ " inner join t_order_detail d on o.iid=d.iorderid  "
			+ " inner join t_order_status s on o.istatus=s.iid "
			+ "where 1=1 "
			+ "<if test=\"list != null\"> and split_part(o.corigin, ':', 1) in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\"  separator=\",\" close=\")\">#{item}</foreach></if>"
			+ "<if test=\"skuornum != null\">and (d.csku=#{skuornum} or o.cordernumber=#{skuornum} ) </if>"
			+ "<if test=\"status != null\"> and  s.iid=#{status}</if>"
			+ "<if test=\"orderStatus != null\">and o.istatus=#{orderStatus} </if>"
			+ "<if test=\"website != null and website != 0\">and o.iwebsiteid=#{website} </if>"
			+ "<if test=\"begindate != null and enddate != null \"> and to_date(to_char(o.dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between #{begindate} and #{enddate} </if>"
			+ " order by o.dcreatedate limit #{pageSize} offset (#{page}-1)*#{pageSize}"
			+ "</script>")
	List<OrderWithDtail> getOrdersWithDetailByLimit(
			Map<String, Object> queryParamMap);

	@Select("<script>select count(distinct(d.iorderid)) from t_order o "
			+ " inner join t_order_detail d on o.iid=d.iorderid "
			+ " inner join t_order_status s on o.istatus=s.iid "
			+ " where 1=1 "
			+ "<if test=\"list != null\"> and  split_part(o.corigin, ':', 1)  in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach></if>"
			+ "<if test=\"skuornum != null\">and (d.csku=#{skuornum} or o.cordernumber=#{skuornum} ) </if>"
			+ "<if test=\"status != null\"> and  s.iid=#{status}</if>"
			+ "<if test=\"orderStatus != null\">and o.istatus=#{orderStatus} </if>"
			+ "<if test=\"website != null and website != 0\">and o.iwebsiteid=#{website} </if>"
			+ "<if test=\"begindate != null and enddate != null \"> and to_date(to_char(o.dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between #{begindate} and #{enddate} </if>"
			+ "</script>")
	int getOrdersWithDetailCount(Map<String, Object> paramMap);

	@Select("<script> select * from t_order where "
			+ "cemail = #{email} and "
			+ "istatus in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>")
	List<Order> getOrderByEmailAndStatus(@Param("email") String email,
			@Param("list") List<Integer> status);

	List<DropShipOrderMessage> searchDropShipOrders(
			@Param("useremail") String useremail,
			@Param("status") List<Integer> status, @Param("start") Date start,
			@Param("end") Date end, @Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("orderNum") String orderNum,
			@Param("cdropshippingid") String cdropshippingid,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost);

	Integer searchDropShipOrderCount(@Param("useremail") String useremail,
			@Param("status") List<Integer> status, @Param("start") Date start,
			@Param("end") Date end, @Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("orderNum") String orderNum,
			@Param("cdropshippingid") String cdropshippingid,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost);

	List<Order> searchOrdersByLabel(@Param("email") String email,
			@Param("status") List<Integer> status, @Param("start") Date start,
			@Param("end") Date end, @Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("orderNumber") String orderNumber,
			@Param("paymentId") String paymentId,
			@Param("transactionId") String transactionId,
			@Param("vhost") String firstName,
			@Param("labelType") String labelType);

	Integer searchCountByLabel(@Param("email") String email,
			@Param("status") List<Integer> status, @Param("start") Date start,
			@Param("end") Date end, @Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("orderNumber") String orderNumber,
			@Param("paymentId") String paymentId,
			@Param("transactionId") String transactionId,
			@Param("vhost") String firstName,
			@Param("labelType") String labelType);

	@Select("<script>select * from t_order "
			+ "where istatus is not null "
			+ "<if test=\"status != null\">and istatus = #{status} </if>"
			+ "<if test=\"start != null\">and (dpaymentdate &gt;= #{start} or dcreatedate &gt;= #{start}) </if>"
			+ "<if test=\"end != null\">and (dpaymentdate &lt;= #{end} or dcreatedate &lt;= #{end}) </if>"
			+ "<if test=\"orderNum != null\">and cordernumber = #{orderNum} </if>"
			+ "<if test=\"paymentId != null\">and cpaymentid = #{paymentId} </if>"
			+ "<if test=\"email != null\">and cemail = #{email} </if>"
			+ "<if test=\"transactionId != null\">and ctransactionid = #{transactionId} </if>"
			+ "<if test=\"vhost != null\">and cvhost = #{vhost} </if>"
			+ "order by dcreatedate limit #{pageSize} offset (#{pageSize} * (#{pageNum} - 1))</script>")
	List<Order> getOrdersByPayData(@Param("status") Integer status,
			@Param("start") Date start, @Param("end") Date end,
			@Param("name") String name, @Param("siteId") Integer siteId,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost);

	@Select("<script>select count(distinct(iid)) from t_order "
			+ "where istatus is not null "
			+ "<if test=\"status != null\">and istatus = #{status} </if>"
			+ "<if test=\"start != null\">and (dpaymentdate &gt;= #{start} or dcreatedate &gt;= #{start}) </if>"
			+ "<if test=\"end != null\">and (dpaymentdate &lt;= #{end} or dcreatedate &lt;= #{end}) </if>"
			+ "<if test=\"orderNum != null\">and cordernumber = #{orderNum} </if>"
			+ "<if test=\"paymentId != null\">and cpaymentid = #{paymentId} </if>"
			+ "<if test=\"email != null\">and cemail = #{email} </if>"
			+ "<if test=\"transactionId != null\">and ctransactionid = #{transactionId} </if>"
			+ "<if test=\"vhost != null\">and cvhost = #{vhost} </if></script>")
	Integer getOrderCountByPayData(@Param("email") String email,
			@Param("status") Integer status, @Param("start") Date start,
			@Param("end") Date end, @Param("siteId") Integer siteId,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost);

	@Select("select distinct(cmemberemail) from t_order where istatus=7 and dcreatedate between #{startDate} and  #{endDate} limit #{pageSize} offset (#{pageSize} * (#{pageNum} - 1))")
	List<String> getAllMemberEmails(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	@Update("<script>update t_order <set>"
			+ "<if test=\"ccountry != null\">ccountry = #{ccountry},</if>"
			+ "<if test=\"ccountrysn != null\">ccountrysn = #{ccountrysn},</if>"
			+ "<if test=\"ccity != null\">ccity = #{ccity},</if>"
			+ "<if test=\"cfirstname != null\">cfirstname = #{cfirstname},</if>"
			+ "<if test=\"cmiddlename != null\">cmiddlename = #{cmiddlename},</if>"
			+ "<if test=\"clastname != null\">clastname = #{clastname},</if>"
			+ "<if test=\"cpostalcode != null\">cpostalcode = #{cpostalcode},</if>"
			+ "<if test=\"cprovince != null\">cprovince = #{cprovince},</if>"
			+ "<if test=\"ctelephone != null\">ctelephone = #{ctelephone},</if>"
			+ "<if test=\"cstreetaddress != null\">cstreetaddress = #{cstreetaddress},</if>"
			+ "<if test=\"cmessage != null\">cmessage = #{cmessage},</if>"
			+ "</set> where cordernumber = #{cordernumber}</script>")
	int updateOrderAddress(Order order);

	@Update("<script>update t_order <set>"
			+ "<if test=\"istorageid != null\">istorageid = #{istorageid},</if>"
			+ "<if test=\"ishippingmethodid != null\">ishippingmethodid = #{ishippingmethodid},</if>"
			+ "<if test=\"fshippingprice != null\">fshippingprice = #{fshippingprice},</if>"
			+ "<if test=\"cshippingcode != null\">cshippingcode = #{cshippingcode},</if>"
			+ "<if test=\"fgrandtotal != null\">fgrandtotal = #{fgrandtotal},</if>"
			+ "</set> where cordernumber = #{cordernumber}</script>")
	int updateShippingMethod(Order order);

	@Update("update t_order set fgrandtotal = #{1}, fshippingprice = #{2} where iid = #{0}")
	int updateOrderShippingPrice(Integer orderId, Double grandtotal,
			Double shippingPrice);

	@Select({ "<script>",
			"select * from t_order where corigin is not NULL and corigin!='' ",
			"<if test=\"sd!=null\">", "and dcreatedate &gt;= #{sd} ", "</if>",
			"<if test=\"ed!=null\">", "and dcreatedate &lt;= #{ed} ", "</if>",
			"order by dcreatedate desc limit #{1} offset #{0}", "</script>" })
	List<OrderApiVo> getOrdersPageForApi(int page, int pageSize,
			@Param("sd") Date sd, @Param("ed") Date ed);

	@Select({
			"<script>",
			"select count(*) from t_order where corigin is not NULL and corigin!='' ",
			"<if test=\"sd!=null\">", "and dcreatedate &gt;= #{sd} ", "</if>",
			"<if test=\"ed!=null\">", "and dcreatedate &lt;= #{ed} ", "</if>",
			"</script>" })
	int getOrdersCountForApi(@Param("sd") Date sd, @Param("ed") Date ed);

	List<DropShipOrderMessage> searchDropShipOrderList(
			@Param("useremail") String useremail,
			@Param("status") List<Integer> status, @Param("start") Date start,
			@Param("end") Date end, @Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost);

	List<Order> searchOrderListByLabel(@Param("email") String email,
			@Param("status") List<Integer> status, @Param("start") Date start,
			@Param("end") Date end, @Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("orderNumber") String orderNumber,
			@Param("paymentId") String paymentId,
			@Param("transactionId") String transactionId,
			@Param("vhost") String firstName,
			@Param("labelType") String labelType);

	/**
	 * @author lijun
	 * @param paras
	 * @return
	 */
	int changeOrdeStatusAndTransactionInfo(Map paras);

	/**
	 * 更新操作
	 * 
	 * @param paras
	 * @return 更新操作影响行数
	 */
	int update(Map paras);

	Integer getOrderTotalCount(@Param("status") List<Integer> list,
			@Param("start") Date start, @Param("end") Date end,
			@Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost, @Param("code") String code);

	Integer getOrdersTransactionTotalCount(@Param("status") Integer status,
			@Param("start") Date start, @Param("end") Date end,
			@Param("siteId") Integer siteId,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost);

	@Update("update t_order set istatus=2,ctransactionid=#{transactionid} where iid = #{iid}")
	Integer updateOrderTransaction(@Param("iid") Integer iid,
			@Param("transactionid") String transactionid);

	/**
	 * 
	 * @param code 
	 * @Title: getOrdersExcludeUser
	 * @Description: TODO(查询需要导出的订单列表(排除测试用户))
	 * @param @param list
	 * @param @param start
	 * @param @param end
	 * @param @param paymentStart
	 * @param @param paymentEnd
	 * @param @param siteId
	 * @param @param orderNum
	 * @param @param paymentId
	 * @param @param email
	 * @param @param transactionId
	 * @param @param vhost
	 * @param @param excUser
	 * @param @return
	 * @return List<Order>
	 * @throws
	 * @author yinfei
	 */
	List<Order> getOrdersExcludeUser(@Param("status") List<Integer> list,
			@Param("start") Date start, @Param("end") Date end,
			@Param("paymentStart") Date paymentStart,
			@Param("paymentEnd") Date paymentEnd,
			@Param("siteId") Integer siteId,
			@Param("orderNum") String orderNum,
			@Param("paymentId") String paymentId, @Param("email") String email,
			@Param("transactionId") String transactionId,
			@Param("vhost") String vhost, @Param("code") String code, @Param("excUser") List<String> excUser);

	@Select({ "<script>", "select d.* from t_order_detail d ",
			"inner join t_order t on t.iid=d.iorderid where t.iid=#{0}",
			"</script>" })
	List<OrderDetail> getOrderDetailByOrderId(Integer oid);

	/**
	 * @author lijun
	 * @param iid
	 * @param transactionid
	 * @return
	 */
	@Update("update t_order set ipaymentstatus=${statusId} where cordernumber = #{orderNum}")
	Integer changeOrdePaymentStatus(@Param("orderNum") String orderNum,
			@Param("statusId") Integer statusId);

	/**
	 * 
	 * @Title: getOrderByMemberAndPayDate
	 * @Description: TODO(通过用户和支付开始时间查询结算金额大于指定金额的订单)
	 * @param @param memberID
	 * @param @param website
	 * @param @param beginTime
	 * @param @param money
	 * @param @return
	 * @return List<Order>
	 * @throws
	 * @author yinfei
	 */
	@Select("select o.* from t_order o join t_order_currency_rate c on "
			+ "o.cordernumber = c.cordernumber "
			+ "where cmemberemail = #{memberID} and iwebsiteid = #{website} and dpaymentdate > #{beginTime} "
			+ "and o.fgrandtotal / c.frate > #{money} and o.istatus in (2,4,5,6,7)")
	List<Order> getOrderByMemberAndPayDate(@Param("memberID") String memberID,
			@Param("website") int website, @Param("beginTime") Date beginTime,
			@Param("money") Double money);

	/**
	 * 后台订单统计功能
	 * 
	 * @param startdate
	 * @param enddate
	 * @param type
	 * @param iwebsiteid
	 * @return
	 */
	@Select({
			"<script>",
			"select count(*) count,sum(o.fgrandtotal/COALESCE(r.frate,ncurr.frate)) sumTotal,istatus status,",
			"to_char(o.dcreatedate,'yyyy-MM-dd') createdate",
			" from t_order o left JOIN t_order_currency_rate r on o.cordernumber = r.cordernumber "
			+ " left join (select ccurrency,max(frate) frate from t_order_currency_rate where dcreatedate> now()- interval'5 day' group by ccurrency) ncurr on o.ccurrency=ncurr.ccurrency "
			+ "where 1=1  ",
			"<if test=\"iwebsiteid!=-1\">",
			" and o.iwebsiteid = #{iwebsiteid}",
			"</if>",
			"<if test=\"cvhost!=null\">",
			" and o.cvhost = #{cvhost}",
			"</if>",
			" and date(o.dcreatedate) between date(#{startdate}) and date(#{enddate}) ",
			"group by to_char(o.dcreatedate,'yyyy-MM-dd'),istatus order by to_char(o.dcreatedate,'yyyy-MM-dd')",
			"</script>" })
	List<OrderReportForm> getOrderReport(@Param("startdate") Date startdate,
			@Param("enddate") Date enddate, @Param("type") String type,
			@Param("iwebsiteid") int iwebsiteid,@Param("cvhost") String cvhost);

}
