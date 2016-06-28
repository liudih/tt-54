package services.base;



import javax.inject.Inject;

import mapper.base.EmailAccountMapper;

import org.springframework.beans.BeanUtils;

import services.IEmailAccountService;
import dto.EmailAccount;
public class EmailAccountService implements IEmailAccountService {
	
	@Inject
	EmailAccountMapper emailAccountMapper;
	
	
	/* (non-Javadoc)
	 * @see services.IEmailAccountService#addEmailAccount()
	 */
	@Override
	public boolean addEmailAccount(){
		EmailAccount emailAccount = new EmailAccount();
		emailAccount.setIwebsiteid(emailAccount.getIwebsiteid()); 
		emailAccount.setCtype(emailAccount.getCtype());
		emailAccount.setCsmtphostName(emailAccount.getCusername());
		emailAccount.setCusername(emailAccount.getCusername());
		emailAccount.setCpassword(emailAccount.getCpassword());
		int result=emailAccountMapper.insert(emailAccount);
		return result>0?true:false;
	}
	
	/* (non-Javadoc)
	 * @see services.IEmailAccountService#getEmailAccount(int)
	 */
	@Override
	public EmailAccount getEmailAccount( int iwebsiteid) {
	    EmailAccount emailAccount = emailAccountMapper.getEmailAccountByIwebsiteid(iwebsiteid);
		return emailAccount;
	}
	
	/* (non-Javadoc)
	 * @see services.IEmailAccountService#getEmailAccount(java.lang.String)
	 */
	@Override
	public EmailAccount getEmailAccount(String ctype) {
		EmailAccount emailAccount = emailAccountMapper
				.getEmailAccountByCtype(ctype);
		return emailAccount;
	}
	
	/* (non-Javadoc)
	 * @see services.IEmailAccountService#deleteEmailAccountByIid(int)
	 */
	@Override
	public boolean deleteEmailAccountByIid(int iid) {
		int result = emailAccountMapper.deleteByPrimaryKey(iid);
		return result > 0 ? true : false;
	}

	/* (non-Javadoc)
	 * @see services.IEmailAccountService#getEmailAccountByiid(int)
	 */
	@Override
	public dto.EmailAccount getEmailAccountByiid(int iid) {
		EmailAccount emailAccount = emailAccountMapper.selectByPrimaryKey(iid);
		dto.EmailAccount EmailAccountVo = new dto.EmailAccount();
		BeanUtils.copyProperties(emailAccount, EmailAccountVo);
		return EmailAccountVo;
	}
}
