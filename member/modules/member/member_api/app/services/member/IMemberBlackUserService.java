package services.member;

import java.util.List;

import dto.member.BlackUser;
import forms.member.memberSearch.MemberSearchForm;

/**
 * 会员黑名单用户服务接口
 * @author guozy
 *
 */
public interface IMemberBlackUserService {

	/**
	 * 添加黑名单的用户
	 * 
	 * @param blackUser
	 * @return
	 */
	public boolean insertBlackUser(BlackUser blackUser);

	/**
	 * 移除黑名单用户信息
	 * 
	 * @param iid
	 * @return
	 */
	public boolean removeBlackUser(String cemail);

	/**
	 * 获取黑名单用户信息
	 * 
	 * @param emails
	 *            TODO
	 * @param blackUser
	 * @return
	 */
	public List<BlackUser> getBlackUser(Integer iwebsiteId, List<String> emails);
	/**
	 * 修改黑名单的数据信息
	 * 
	 * @param blackUser
	 * @return
	 */
	public boolean updateBlackUser(BlackUser blackUser);

	/**
	 * 获取黑名单用户邮箱
	 * 
	 * @return
	 */
	public BlackUser getBlackUserEamil(String cemail);
}
