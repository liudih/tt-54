package mapper.google.category;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.product.google.category.AttributeKey;

public interface AttributeKeyMapper {

	@Select("select * from t_google_attribute_key where iid = #{0}")
	AttributeKey getKeyById(Integer iattributekeyid);

	@Insert("insert into t_google_attribute_key (ckeyname) values (#{0})")
	int insertKey(String ckeyname);

	@Select("select * from t_google_attribute_key where ckeyname = #{0} ")
	AttributeKey selectIidByName(String ckeyname);

	@Delete("delete from t_google_attribute_key where iid = #{0}")
	int delById(int id);

	@Select("select * from t_google_attribute_key where ckeyname = #{0}")
	AttributeKey getAttrByName(String ckeyname);

}
