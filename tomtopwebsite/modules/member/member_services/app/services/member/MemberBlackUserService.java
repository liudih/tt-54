package services.member;

import java.util.List;

import javax.inject.Inject;

import dto.member.BlackUser;
import mapper.member.BlackUserMapper;

/**
 * 会员黑名单用户服务接口实现类
 * @author guozy
 *
 */
public class MemberBlackUserService implements IMemberBlackUserService{

	@Inject
	private BlackUserMapper blackUserMapper;

	/**
	 * 添加黑名单的用户
	 * 
	 * @param blackUser
	 * @return
	 */
	@Override
	public boolean insertBlackUser(BlackUser blackUser) {
		int result = blackUserMapper.insertBlackUser(blackUser);
		return result > 0 ? true : false;
	}
	

	/**
	 * 移除黑名单用户信息
	 * 
	 * @param iid
	 * @return
	 */
	@Override
	public boolean removeBlackUser(String cemail) {
		int result = blackUserMapper.removeBlackUser(cemail);
		return result > 0 ? true : false;
	}

	/**
	 * 获取黑名单用户信息
	 * 
	 * @param emails
	 *            TODO
	 * @param blackUser
	 * @return
	 */
	@Override
	public List<BlackUser> getBlackUser(Integer iwebsiteId, List<String> emails) {
		return blackUserMapper.getBlackUser(iwebsiteId, emails);
	};

	/**
	 * 修改黑名单的数据信息
	 * 
	 * @param blackUser
	 * @return
	 */
	@Override
	public boolean updateBlackUser(BlackUser blackUser) {
		int result = blackUserMapper.updateBlackUser(blackUser);
		return result > 0 ? true : false;
	};

	/**
	 * 获取黑名单用户邮箱
	 * 
	 * @return
	 */
	@Override
	public BlackUser getBlackUserEamil(String cemail) {
		return blackUserMapper.getBlackUserEmail(cemail);
	}
}
