package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.mybatis.guice.transactional.Transactional;

import entity.manager.AdminRole;

public interface AdminRoleMapper {

	@Transactional
	int deleteByPrimaryKey(Long iid);

	@Transactional
	int insert(AdminRole record);

	AdminRole selectByPrimaryKey(Long iid);

	@Select("select * from t_admin_role a order by iid asc")
	List<AdminRole> getAllAdminRole();

	@Transactional
	int updateByPrimaryKey(AdminRole record);

	@Select("select count(iid) from t_admin_user_role_map where iroleid = #{0}")
	int validateExistUser(Long iid);

	@Select("select r.* from t_admin_role r left join t_admin_user_role_map m on r.iid=m.iroleid where m.iuserid=#{0}")
	AdminRole getAdminRoleByAdminId(int iid);

}