package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import entity.manager.AdminUserWebsitMap;

public interface AdminUserWebsitMapMapper {
	@Select("select iwebsiteid from t_admin_user_website_map where iuserid = #{0}")
	List<Integer> getWebsiteIdByUserId(Integer userId);

	@Insert("insert into t_admin_user_website_map(iuserid, iwebsiteid, ccreateuser, dcreatedate) "
			+ "values(#{iuserid}, #{iwebsiteid}, #{ccreateuser}, #{dcreatedate})")
	int insert(AdminUserWebsitMap adminUserWebsitMap);

	@Delete("delete from t_admin_user_website_map where iuserid = #{0  }")
	int deleteByUserId(Integer userId);

	@Insert({ "<script> insert into t_admin_user_website_map(iuserid, iwebsiteid, ccreateuser, dcreatedate) values "
			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\"> "
			+ " (#{item.iuserid,jdbcType=INTEGER}, "
			+ " #{item.iwebsiteid,jdbcType=INTEGER}, "
			+ " #{item.ccreateuser,jdbcType=VARCHAR}, "
			+ " #{item.dcreatedate,jdbcType=TIMESTAMP})"
			+ " </foreach> </script>" })
	int insertBatch(List<AdminUserWebsitMap> list);
}