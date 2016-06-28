package mapper.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import valueobjects.order_api.cart.CartItem;
import dto.TopBrowseAndSaleCount;
import dto.api.OrderDetailApiVo;
import dto.order.OrderDetail;
import dto.order.ProductStatistics;
import dto.product.SimpleProductBase;

public interface DetailMapper {

	int batchInsert(List<OrderDetail> list);

	@Select("select * from t_order_detail where iorderid = #{0}")
	List<OrderDetail> getOrderDetailByOrderId(Integer orderid);

	@Select("select * from t_order_detail where cid = #{0}")
	OrderDetail getOrderDetailByCid(String cid);

	@Select("select sum(iqty) from t_order_detail where iorderid = #{0}")
	Integer getSumByOrderId(Integer orderId);

	@Select("select clistingid from t_order_detail where iorderid in (select iorderid from t_order_detail where clistingid=#{0} "
			+ "group by iorderid having count(1)<#{1}) and clistingid!=#{0}  group by clistingid having  count(1)>#{2}")
	List<String> getBundlingListingFordetail(String listingid,
			Integer wholesalejudge, Integer count);

	@Delete({ "<script> delete from t_order_detail where iorderid in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	int deleteByOrderId(@Param("list") List<Integer> orderid);

	@Select("select sum(d.iqty) as qty, "
			+ "d.clistingid as listingid, "
			+ "o.iwebsiteid siteid, "
			+ "1 as categoryid "
			+ "from t_order_detail d inner join t_order o on o.iid = d.iorderid "
			+ "group by clistingid, siteid")
	List<ProductStatistics> getProductStatistics();

	@Update("update t_order_detail set clistingid=#{clistingid} where csku=#{csku}")
	int initOldListingidForSku(SimpleProductBase pb);

	@Select("select * from t_order_detail d "
			+ "inner join t_order o on o.iid=d.iorderid and o.cmemberemail = #{0} and o.istatus = #{2} "
			+ "where d.commentid is null and d.clistingid=#{1} order by d.dcreatedate desc limit 1")
	OrderDetail getOrderDetailNotCommentByEmail(String email, String listingId,
			Integer status);

	@Select("select * from t_order_detail d "
			+ "inner join t_order o on o.iid=d.iorderid and o.cmemberemail = #{0} and o.istatus = #{2} "
			+ "where d.clistingid=#{1} order by d.dcreatedate desc")
	List<OrderDetail> getOrderDetailsForPhotoByEmail(String email,
			String listingId, Integer status);

	@Select("select clistingid as listingid,count(1) as salecount "
			+ "from t_order_detail where now() between dcreatedate and dcreatedate + interval '${timeRange} day' "
			+ "group by clistingid ")
	List<TopBrowseAndSaleCount> getTopSaleByTimeRange(
			@Param("timeRange") Integer timeRange);

	@Select({ "<script> select * from t_order_detail where iorderid in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	List<OrderDetail> getDetailByOrderIds(@Param("list") List<Integer> orderids);

	@Update("update t_order_detail set commentid=#{0} where cid=#{1}")
	int updateDetailCommentId(Integer commentId, String cid);

	@Select({
			"<script>",
			"select * from t_order_detail where iorderid in ",
			"<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">#{item}</foreach> ",
			"</script>" })
	List<OrderDetailApiVo> getOrderDetailsForApi(
			@Param("list") List<Integer> ids);

	/**
	 * 反序列化order
	 * 
	 * @author lijun
	 * @param paras
	 * @return
	 */
	List<CartItem> selectCartItemsByOrderNum(Map paras);

	/**
	 * 根具sku的相关信息获取列表，此方法提供给sku销售报表分而使用
	 * 
	 * @return
	 */
	@Select({
			"<script>",
			"select * from ",
			"(select t5.csku,t5.fprice,sum(t5.iqty) as iqty,t5.fprice*sum(t5.iqty) ftotalprices from ",
			"(select t1.csku,sum(t1.iqty) as iqty,case when t2.frate is not null then (t2.frate * t1.fprice) else t1.fprice end as fprice",
			" from t_order_detail t1 inner join t_order t3 on t1.iorderid=t3.iid",
			" left join t_order_currency_rate t2 on t3.cordernumber=t2.cordernumber ",
			" where 1=1",
			" <if test=\"csku != null and csku.size() > 0 \">",
			" and t1.csku in <foreach item=\"sku\" collection=\"csku\" open=\"(\" separator=\",\" close=\")\">#{sku}</foreach>",
			" </if>",
			" <if test=\"dpaymentstartdate != null\"> and t3.dpaymentdate &gt;= #{dpaymentstartdate}</if>",
			" <if test=\"dpaymentenddate != null\">and t3.dpaymentdate &lt;= #{dpaymentenddate}</if>",
			" <if test=\"createStartDate != null\"> and t3.dcreatedate &gt;= #{createStartDate}</if>",
			" <if test=\"createEndDate != null\">and t3.dcreatedate &lt;= #{createEndDate}</if>",
			" <if test=\"istatus != null\"> and t3.istatus in",
			" <foreach item=\"item1\" collection=\"istatus\" open=\"(\" separator=\",\" close=\")\">#{item1,jdbcType=INTEGER}</foreach></if> ",
			" <if test=\"cvhosts != null\"> and t3.cvhost in",
			" <foreach item=\"item2\" collection=\"cvhosts\" open=\"(\" separator=\",\" close=\")\">#{item2,jdbcType=VARCHAR}</foreach></if> ",
			" GROUP BY t1.csku,t1.fprice,t2.frate) t5",
			" GROUP BY t5.csku,t5.fprice) t6",
			"order by csku",
			" <if test=\"p != -1\"> limit #{pagesize} offset (#{p}-1)*#{pagesize} </if>",
			"</script>" })
	public List<OrderDetail> getPageBySkuInfo(@Param("p") int p,
			@Param("pagesize") int pagesize, @Param("csku") List<String> csku,
			@Param("istatus") List<Integer> istatus,
			@Param("cvhosts") List<String> cvhosts,
			@Param("dpaymentstartdate") Date dpaymentstartdate,
			@Param("dpaymentenddate") Date dpaymentenddate,
			@Param("createStartDate") Date createStartDate,
			@Param("createEndDate") Date createEndDate);

	/**
	 * 根具sku的相关信息获取列表，此方法提供给sku销售报表分而使用,获取总数
	 * 
	 * @param query
	 * @return
	 */
	@Select({
			"<script>",
			"select count(*) from ",
			"(select t5.csku,t5.fprice,sum(t5.iqty) as iqty,t5.fprice*sum(t5.iqty) salesamount from ",
			"(select t1.csku,sum(t1.iqty) as iqty,case when t2.frate is not null then (t2.frate * t1.fprice) else t1.fprice end as fprice",
			" from t_order_detail t1 inner join t_order t3 on t1.iorderid=t3.iid",
			" left join t_order_currency_rate t2 on t3.cordernumber=t2.cordernumber ",
			" where 1=1",
			" <if test=\"csku != null and csku.size() > 0 \">",
			" and t1.csku in <foreach item=\"sku\" collection=\"csku\" open=\"(\" separator=\",\" close=\")\">#{sku}</foreach>",
			" </if>",
			" <if test=\"dpaymentstartdate != null\"> and t3.dpaymentdate &gt;= #{dpaymentstartdate}</if>",
			" <if test=\"dpaymentenddate != null\">and t3.dpaymentdate &lt;= #{dpaymentenddate}</if>",
			" <if test=\"createStartDate != null\"> and t3.dcreatedate &gt;= #{createStartDate}</if>",
			" <if test=\"createEndDate != null\">and t3.dcreatedate &lt;= #{createEndDate}</if>",
			" <if test=\"istatus != null\"> and t3.istatus in",
			" <foreach item=\"item1\" collection=\"istatus\" open=\"(\" separator=\",\" close=\")\">#{item1,jdbcType=INTEGER}</foreach></if> ",
			" <if test=\"cvhosts != null\"> and t3.cvhost in",
			" <foreach item=\"item2\" collection=\"cvhosts\" open=\"(\" separator=\",\" close=\")\">#{item2,jdbcType=VARCHAR}</foreach></if> ",
			" GROUP BY t1.csku,t1.fprice,t2.frate) t5",
			" GROUP BY t5.csku,t5.fprice) t6", "</script>" })
	public int getCountBySkuInfo(@Param("csku") List<String> csku,
			@Param("istatus") List<Integer> istatus,
			@Param("cvhosts") List<String> cvhosts,
			@Param("dpaymentstartdate") Date dpaymentstartdate,
			@Param("dpaymentenddate") Date dpaymentenddate,
			@Param("createStartDate") Date createStartDate,
			@Param("createEndDate") Date createEndDate);
}
