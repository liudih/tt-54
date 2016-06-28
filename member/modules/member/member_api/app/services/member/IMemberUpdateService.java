package services.member;

import java.util.List;

import context.WebContext;
import dto.member.MemberBase;
import dto.member.MemberByStatistics;
import forms.member.register.RegisterUpdateForm;

public interface IMemberUpdateService {

	public abstract MemberBase getUserByAccount(String account);

	public abstract boolean savaMember(RegisterUpdateForm memberForm,
			WebContext context);

	public abstract void saveMember(MemberBase base);

	public abstract void saveBatchMember(List<MemberBase> members);

	public abstract void SaveBuyStatistics(MemberByStatistics memberByStatistics);

	public abstract MemberBase getMemberByEmail(String email, WebContext context);

	public abstract String save(com.website.dto.member.Member[] members);

	public abstract void SaveMemberAccount(String email);

	public abstract boolean SaveMemberPasswd(String email, String cpasswd, WebContext webContext);

	public abstract boolean updateMember(MemberBase mmbase);
	
	public dto.member.MemberBase getMemberDto(String email,WebContext context);
	
	/**
	 * @author lijun
	 * @param token 数据库中的随机数
	 * @param newPassword 新密码
	 * @param webContext
	 * @return
	 */
	public boolean changePassword(String token, String newPassword, WebContext webContext);

}