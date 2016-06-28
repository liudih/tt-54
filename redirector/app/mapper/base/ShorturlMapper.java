package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import entity.base.Shorturl;

public interface ShorturlMapper {
	
	@Select("select * from t_shorturl")
	List<Shorturl> getShorturls();
	
	int addShorturl(Shorturl s);
	
	@Select("select * from t_shorturl where cshorturlcode=#{0} order by iid desc limit 1")
	Shorturl getShorturlBySurl(String surl);
}
