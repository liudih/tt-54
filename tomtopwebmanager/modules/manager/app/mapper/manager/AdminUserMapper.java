package mapper.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.manager.AdminUser;

public interface AdminUserMapper {

	@Select("select * from t_admin_user where cusername=#{0} and cpasswd=#{1} limit 1")
	AdminUser getAdminUser(String userString, String passwdString);

	@Select("select * from t_admin_user where cjobnumber=#{0} limit 1")
	AdminUser getAdminByJobnumber(String num);
	
	@Select("select * from t_admin_user")
	List<AdminUser> getAdminUserAll();

	int deleteByPrimaryKey(Integer iid);

	int insert(AdminUser record);

	int insertSelective(AdminUser record);

	AdminUser selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(AdminUser record);

	int updateByPrimaryKey(AdminUser record);

	@Select("select * from t_admin_user limit #{1} offset (#{0}-1)*#{1}")
	List<AdminUser> getAdminUserPage(int page, int pageSize);

	@Select("select count(iid) from t_admin_user ")
	int getAdminUserCount();

	List<AdminUser> searchAdminUserPage(Map<String, Object> param);

	Integer searchAdminUserCount(Map<String, Object> param);

	@Select({
			"<script>",
			"select * from t_admin_user ",
			"<if test=\"list!=null and list.size()>0\">",
			"where iid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</if>", "</script>" })
	List<AdminUser> getAdminUserList(@Param("list") List<Integer> ids);

	@Select("select * from t_admin_user ")
	List<AdminUser> getAllAdminUser();

	@Select("select count(iid) from t_admin_user where cjobnumber=#{0}")
	int validateJobNumber(String cjobnumber);

	@Select("select count(iid) from t_admin_user where cusername=#{0}")
	int validateUserName(String cusername);

	@Select("select iid from t_admin_user where cusername=#{0}")
	Integer getAdminUserIdByName(String cusername);

	@Select("select * from t_admin_user where cjobnumber=#{0} limit 1")
	dto.AdminUser getAdminUserByJobnumber(String num);
}
