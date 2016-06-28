package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.guice.transactional.Transactional;

import entity.manager.MenuRoleMap;

public interface AdminMenuRoleMapper {

	@Select("select count(iid) from t_admin_menu_role_map t where iroleid = #{0} and imenuid=#{1}")
	Integer validate(Integer roleid, Integer imenuid);

	@Select("select iroleid from t_admin_user_role_map where iuserid = #{0}")
	List<Integer> getRoleIdByUserId(Integer userId);

	@Select("select iid from t_admin_menu where cenname = #{0}")
	Integer getMenuIdByMenuName(String menuName);

	@Transactional
	int deleteByPrimaryKey(Long iid);

	@Transactional
	int deleteMenuRoleByRoleId(Integer iroleid);

	@Transactional
	int insert(MenuRoleMap record);

	MenuRoleMap selectByPrimaryKey(Long iid);

	@Select("select * from t_admin_menu_role_map a order by iid asc")
	List<MenuRoleMap> getAllMenuRoleMapMap();

	@Transactional
	int updateByPrimaryKey(MenuRoleMap record);

	@Select("select imenuid from t_admin_menu_role_map t where iroleid = #{0}")
	List<Integer> getMenuIdsByRoleId(Integer roleid);

	List<Integer> getMenuIdsByRoleIds(@Param("list") List<Integer> roleids);
}
