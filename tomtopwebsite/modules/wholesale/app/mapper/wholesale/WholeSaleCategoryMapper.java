package mapper.wholesale;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import entity.wholesale.WholeSaleCategory;


public interface WholeSaleCategoryMapper {
	@Delete("delete from t_wholesale_category where iwholesaleid = #{0}")
	int deleteByWholeSaleId(Integer wholeSaleId);

	@Insert("insert into t_wholesale_category (iwholesaleid, icategoryid) "
			+ "values (#{iwholesaleid},#{icategoryid})")
	int addWholeSaleCategory(WholeSaleCategory record);
	
	@Select("select * from t_wholesale_category where iwholesaleid = #{0}")
	List<WholeSaleCategory> getWholeSaleCategoryByWholeSaleId(Integer wholeSaleId);
}