package com.tomtop.product.mappers.base;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tomtop.product.models.dto.price.CurrencyRate;

public interface CurrencyRateMapper {

	@Select("select * from t_currency_rate "
			+ "where buse=true and ccode = #{0} and dcreatedate < #{1} "
			+ "order by dcreatedate limit 1")
	CurrencyRate findLatestRateByCode(String code, Date now);

	@Insert("insert into t_currency_rate(ccode,fexchangerate) values(#{ccode},#{fexchangerate})")
	int Insert(CurrencyRate currencyRate);

	@Select("select * from t_currency_rate where iid in (select max(iid) from t_currency_rate group by ccode)")
	List<CurrencyRate> findLatestRate();

	@Select("select * from t_currency_rate where buse = true")
	List<CurrencyRate> findUsedRate();

	@Select("select * from t_currency_rate where ccode = #{0} order by iid desc limit 1")
	CurrencyRate getLatestRateByCode(String code);

	@Update("update t_currency_rate set buse = false where ccode = #{0}")
	int banRateByCode(String code);

	@Update("update t_currency_rate set buse = true where iid = #{0}")
	int useLatestRateByCode(Integer iid);

	@Update("update t_currency_rate set fexchangerate = #{0} where iid = #{1}")
	int updateRate(Double rate, Integer iid);

	@Select("select * from t_currency_rate where iid = #{0} limit 1")
	CurrencyRate findById(Integer id);
}
