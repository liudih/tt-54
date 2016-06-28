package mapper.image;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.image.ImgUseMapping;

public interface ImgUseMappingMapper {

	@Insert("insert into t_img_use_mapping (iimgid, cpath, clabel, ccreateuser) "
			+ "values (#{iimgid}, #{cpath}, #{clabel}, #{ccreateuser})")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int addNewImgUseMapping(ImgUseMapping img);

	@Select("select * from t_img_use_mapping where cpath=#{0} and clabel=#{1}")
	ImgUseMapping getImgUseMapping(String cpath, String clabel);

	@Select({
			"<script>",
			"select * from t_img_use_mapping ",
			"where iimgid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	List<ImgUseMapping> getImgUseMappingByImgIds(
			@Param("list") List<Long> imgids);

	@Delete("delete from t_img_use_mapping where iimgid=#{0}")
	int deleteImgUserMappingByFileId(Integer fileId);
	
	@Select("select * from t_img_use_mapping where  clabel=#{0}")
	List<ImgUseMapping> getImgUseMappingByClabel(String clabel);

}
