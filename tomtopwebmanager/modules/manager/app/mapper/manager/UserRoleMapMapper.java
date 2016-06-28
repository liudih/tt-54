package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.mybatis.guice.transactional.Transactional;

import entity.manager.UserRoleMap;

public interface UserRoleMapMapper {

	@Transactional
	int deleteByPrimaryKey(Long iid);
	
	@Transactional
	int deleteUserRoleByUserId(Integer iuserid);

	@Transactional
	int insert(UserRoleMap record);

	UserRoleMap selectByPrimaryKey(Long iid);

	@Select("select * from t_admin_user_role_map a order by iid asc")
	List<UserRoleMap> getAllUserRoleMap();

	@Transactional
	int updateByPrimaryKey(UserRoleMap record);

}