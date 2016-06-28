package services.base;

import java.util.List;

import javax.inject.Inject;

import dto.LoginTerminal;
import mapper.base.LoginTerminalMapper;

/**
 * 登录终端
 * 
 * @author xiaoch
 *
 */
public class LoginTerminalService {

	@Inject
	LoginTerminalMapper loginTerminalMapper;

	/**
	 * 获取所有登陆终端列表
	 * 
	 * @return
	 */
	public List<LoginTerminal> getAll() {
		return loginTerminalMapper.getAll();
	}

}
