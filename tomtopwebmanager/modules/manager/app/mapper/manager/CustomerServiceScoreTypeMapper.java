package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.manager.CustomerServiceScoreType;

public interface CustomerServiceScoreTypeMapper {
	@Insert("insert into t_customer_service_score_type (cname, cdescription,ilanguageid,ipriority) values (#{cname}, #{cdescription},#{ilanguageid},#{ipriority})")
	int insert(CustomerServiceScoreType type);

	@Select("select * from t_customer_service_score_type order by dcreatedate desc limit #{1} offset (#{0} - 1) * #{1}")
	List<CustomerServiceScoreType> getPage(int page, int pageSize);

	@Delete("delete from t_customer_service_score_type where iid = #{0}")
	int deleteByID(int id);

	@Update("<script>update t_customer_service_score_type "
			+ "<set><if test=\"cname != null\" >cname = #{cname}, </if>"
			+ "<if test=\"cdescription != null\" >cdescription = #{cdescription}, </if></set>"
			+ "where iid = #{iid}</script>")
	int update(CustomerServiceScoreType type);

	@Select("select * from t_customer_service_score_type where iid = #{0}")
	CustomerServiceScoreType getByID(int id);

	@Select("select count(iid) from t_customer_service_score_type")
	int count();

	@Select("select * from t_customer_service_score_type")
	List<CustomerServiceScoreType> getAll();

	@Select("select * from t_customer_service_score_type where ilanguageid=#{0}")
	List<CustomerServiceScoreType> getScoreTypeByLanguageId(int languageId);
}
