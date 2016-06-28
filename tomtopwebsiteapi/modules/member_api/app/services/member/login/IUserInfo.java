package services.member.login;

/**
 * facebook or google logon user info 这只是一个标识接口
 * 
 * @author liuxin
 *
 */
public interface IUserInfo {
	public String getId();
	public String getEmail();
}
