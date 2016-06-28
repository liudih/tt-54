package services.member.forgetpasswd;

import java.util.Date;

import javax.inject.Inject;

import context.WebContext;
import mapper.member.ForgetPasswdBaseMapper;
import services.base.FoundationService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import dto.member.ForgetPasswdBase;

public class ForgetPasswdBaseService implements IForgetPasswdBaseService {
	@Inject
	ForgetPasswdBaseMapper forgetPasswdBaseMapper;

	@Inject
	FoundationService foundationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.forgetpasswd.IForgetPasswdBaseService#getForgetPasswdBase
	 * (java.lang.String)
	 */
	@Override
	public ForgetPasswdBase getForgetPasswdBase(String cmemberemail,
			Integer websiteId) {
		return forgetPasswdBaseMapper.getForgetPasswdBaseByCmembermail(
				cmemberemail, websiteId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.forgetpasswd.IForgetPasswdBaseService#update(dto.member
	 * .ForgetPasswdBase)
	 */
	@Override
	public boolean update(ForgetPasswdBase users) {
		int result = forgetPasswdBaseMapper.updateByPrimaryKey(users);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.forgetpasswd.IForgetPasswdBaseService#insert(dto.member
	 * .ForgetPasswdBase)
	 */
	@Override
	public boolean insert(ForgetPasswdBase users) {
		int result = forgetPasswdBaseMapper.insert(users);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.forgetpasswd.IForgetPasswdBaseService#getEmail(java.lang
	 * .String)
	 */
	@Override
	public String getEmail(String cid) {
		return forgetPasswdBaseMapper.getEmailByCid(cid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.forgetpasswd.IForgetPasswdBaseService#getForgetPwdByCode
	 * (java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ForgetPasswdBase getForgetPwdByCode(String email, String code,
			boolean buse) {

		return forgetPasswdBaseMapper.getForgetPwdByCode(email, code, buse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.forgetpasswd.IForgetPasswdBaseService#deleteByEmail(java
	 * .lang.String)
	 */
	@Override
	public boolean deleteByEmail(String email, WebContext webContext) {
		int websiteId = foundationService.getSiteID(webContext);
		int result = forgetPasswdBaseMapper.deleteByEmail(email, websiteId);
		return result > 0 ? true : false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.forgetpasswd.IForgetPasswdBaseService#getCount(java.lang
	 * .String, java.util.Date, java.util.Date)
	 */
	@Override
	public Integer getCount(String email, Date startDate, Date endDate,
			WebContext webContext) {
		int websiteId = foundationService.getSiteID(webContext);
		return forgetPasswdBaseMapper.getCountByCmembermailAndDcreatedate(
				email, startDate, endDate, websiteId);
	}

	@Override
	public boolean update(boolean buse, String cid) {
		return forgetPasswdBaseMapper.update(buse, cid) > 0 ? true
				: false;
	}
	
	/**
	 * @author lijun
	 * 检查是否失效
	 * @param token
	 * @return
	 */
	public boolean isFail(String token){
		if(token == null || token.length() == 0){
			throw new NullPointerException("token is null");
		}
		return !forgetPasswdBaseMapper.isFail(token);
	}
}
