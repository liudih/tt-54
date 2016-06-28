package services;

import dto.EmailAccount;

public interface IEmailAccountService {

	public abstract boolean addEmailAccount();

	public abstract EmailAccount getEmailAccount(int iwebsiteid);

	/**
	 * 根据邮件协议查找
	 * @param ctype
	 * @return
	 */
	public abstract EmailAccount getEmailAccount(String ctype);

	public abstract boolean deleteEmailAccountByIid(int iid);

	public abstract dto.EmailAccount getEmailAccountByiid(int iid);

}