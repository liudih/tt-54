package mappers.tracking;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.api.VisitLogApiVo;
import entity.tracking.VisitLog;

public interface VisitLogMapper {

	@Insert("INSERT INTO t_visit_log (caid, iwebsiteid, csource, cip, cpath, dcreatedate, ceid,itasktype)"
			+ "VALUES (#{caid}, #{iwebsiteid}, #{csource}, #{cip}, #{cpath}, #{dcreatedate}, #{ceid}"
			+ ",#{itasktype} )")
	int insert(VisitLog record);

	@Select("select * from t_visit_log where caid=#{0} and dcreatedate>=#{1} "
			+ "and dcreatedate<#{2} order by dcreatedate desc limit 1")
	VisitLog getVisitLogByAid(String aid, Date sd, Date ed);

	@Select({
			"<script>",
			"select * from t_visit_log where caid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"<if test=\"sd!=null and sd!=''\">",
			"and dcreatedate &gt;= #{sd} ", "</if>",
			"<if test=\"ed!=null and ed!=''\">",
			"and dcreatedate &lt;= #{ed} ", "</if>",
			"order by dcreatedate asc",
			"</script>" })
	List<VisitLog> getVisitLogByAids(@Param("list") List<String> aids,
			@Param("sd") Date startdate, @Param("ed") Date enddate);

	@Select("<script> select caid, iwebsiteid, csource, cip, cpath, dcreatedate from "
			+ " t_visit_log  where caid=#{aid} "
			+ "<if test=\"word != null \"> and (csource=#{word} or cpath=#{word})</if>"
			+ "<if test=\"begindate != null and enddate != null \"> and to_date(to_char(dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between #{begindate} and #{enddate} </if>"
			+ " order by dcreatedate limit "
			+ " #{pageSize} offset (#{page}-1)*#{pageSize} " + " </script>")
	List<VisitLog> getVisitLogLimitByParamMap(Map<String, Object> queryParamMap);

	@Select("<script> select count(caid) from "
			+ " t_visit_log  where caid=#{aid} "
			+ "<if test=\"word != null  \"> and csource=#{word} </if>"
			+ "<if test=\"begindate != null and enddate != null \"> and to_date(to_char(dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between #{begindate} and #{enddate} </if>"
			+ " </script>")
	int getVisitLogCountByParamMap(Map<String, Object> queryParamMap);

	@Select("<script> select caid, iwebsiteid, csource, cip, cpath, dcreatedate from "
			+ " t_visit_log  where caid=#{aid} "
			+ "<if test=\"word != null \"> and (csource=#{word} or cpath=#{word})</if>"
			+ "<if test=\"begindate != null and enddate != null \"> and to_date(to_char(dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between #{begindate} and #{enddate} </if>"
			+ " order by dcreatedate " + " </script>")
	List<VisitLog> getVisitLogByParamMap(Map<String, Object> queryParamMap);

	@Select({
			"<script>",
			"select * from t_visit_log where 1=1 ",
			"<if test=\"sd!=null and sd!=''\">",
			"and dcreatedate &gt;= #{sd} ",
			"</if>",
			"<if test=\"ed!=null and ed!=''\">",
			"and dcreatedate &lt;= #{ed} ",
			"</if>",
			"<if test=\"sr!=null and sr!=''\">",
			"and csource like '%'||trim(#{sr})||'%' ",
			"</if>",
			"<if test=\"ld!=null and ld!=''\">",
			"and cpath like '%'||trim(#{ld})||'%' ",
			"</if>",
			"<if test=\"aidarr!=null and aidarr.size()>0\">",
			"and caid in ",
			"<foreach item='item' index='index' collection='aidarr' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</if>", 
			"and caid is not null and caid!='' ",
			"<if test=\"pg!=-1\">", "limit #{1} offset #{pg}",
			"</if>",
			"</script>" })
	List<VisitLog> getvisitLogPage(@Param("pg") int page, int pageSize,
			@Param("sd") Date startDate, @Param("ed") Date endDate,
			@Param("sr") String source,
			@Param("ld") String landing, @Param("aidarr") List<String> aidarr);

	@Select({
			"<script>",
			"select count(*) from t_visit_log where 1=1 ",
			"<if test=\"sd!=null and sd!=''\">",
			"and dcreatedate &gt;= #{sd} ",
			"</if>",
			"<if test=\"ed!=null and ed!=''\">",
			"and dcreatedate &lt;= #{ed} ",
			"</if>",
			"<if test=\"sr!=null and sr!=''\">",
			"and csource like '%'||trim(#{sr})||'%' ",
			"</if>",
			"<if test=\"ld!=null and ld!=''\">",
			"and cpath like '%'||trim(#{ld})||'%' ",
			"</if>",
			"<if test=\"aidarr!=null and aidarr.size()>0\">",
			"and caid in ",
			"<foreach item='item' index='index' collection='aidarr' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</if>", 
			"and caid is not null and caid!='' ",
			"</script>" })
	int getvisitLogCount(@Param("sd") Date startDate,
			@Param("ed") Date endDate,
			@Param("sr") String source, @Param("ld") String landing,
			@Param("aidarr") List<String> aidarr);

	@Select("<script> select caid, iwebsiteid, csource, cip, cpath, dcreatedate from "
			+ " t_visit_log  where caid=#{0} "
			+ "<if test=\"#{1} != null and #{2} != null \"> and to_date(to_char(dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between #{1} and #{2} </if>"
			+ " </script>")
	List<VisitLog> getVisitLogsByAid(String caid, Date begindate, Date enddate);

	@Select("<script> select caid, iwebsiteid, csource, cip, cpath, dcreatedate from "
			+ " t_visit_log  where 1=1 "
			+ "<if test=\"#{0} != null and #{1} != null \"> and to_date(to_char(dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between #{0} and #{1} </if>"
			+ "</script>")
	List<VisitLog> getVisitLogsByDateRange(Date begindate, Date enddate);
	
	@Select("select * from t_visit_log where caid is not NULL and caid!='' "+ 
			"and dcreatedate >= #{2} and dcreatedate <= #{3} "+
			"order by dcreatedate asc "+
			"limit #{1} offset #{0} ")
	List<VisitLogApiVo> getVisitLogPageForApi(Integer page, Integer pageSize,
			Date sd, Date ed);
	
	@Select("select count(*) from t_visit_log where caid is not NULL and caid!='' "+ 
			"and dcreatedate >= #{0} and dcreatedate <= #{1} ")
	int getVisitLogCountForApi(Date sd, Date ed);
	
	@Select("<script> select count(caid) from "
			+ " t_visit_log  where caid=#{0} "
			+ "<if test=\"#{1} != null and #{2} != null \"> and to_date(to_char(dcreatedate,'DD/MM/YYYY'),'DD/MM/YYYY') between #{1} and #{2} </if>"
			+ " </script>")
	int getVisitLogsByAidCount(String caid, Date begindate, Date enddate);
}
