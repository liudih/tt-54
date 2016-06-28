package com.rabbit.conf.basemapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;


public interface BaseMapper {

	/**
	 * 查询出所有语言
	 * 
	 * @return List<Language>
	 */
	@Select("select iid, cname from t_language order by iid")
	List<com.rabbit.dto.base.SimpleLanguage> getAllSimpleLanguages();

}
