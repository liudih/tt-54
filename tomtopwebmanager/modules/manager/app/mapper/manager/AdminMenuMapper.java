package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.manager.AdminMenu;

public interface AdminMenuMapper {

	@Select("select tmenu.iid,ilevel,cname,cclass,iparentid,tmenu.curl from t_admin_menu tmenu "
			+ "inner join t_admin_menu_role_map tmenumap on tmenu.iid = tmenumap.imenuid "
			+ "inner join t_admin_user_role_map tusermap on tmenumap.iroleid = tusermap.iroleid "
			+ "where iuserid=#{0}")
	List<AdminMenu> getMenuForUserId(int userid);

	@Select("select tmenu.iid,ilevel,cname,cclass,iparentid,tmenu.curl from t_admin_menu tmenu")
	List<AdminMenu> getAllMenu();

	List<AdminMenu> getAdminMenuByMenuIds(@Param("list") List<Integer> imenuids);
}
