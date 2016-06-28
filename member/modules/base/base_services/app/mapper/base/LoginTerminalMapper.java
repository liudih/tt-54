package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.LoginTerminal;

public interface LoginTerminalMapper {

	@Select("select * from t_login_terminal")
	List<LoginTerminal> getAll();

}
