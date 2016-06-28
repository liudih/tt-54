package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.Currency;
import dto.CurrencyRate;

public interface CurrencyMapper {

	@Select("select * from t_currency where ccode = #{0} ")
	Currency findByCode(String code);
	
	@Select("select * from t_currency where ccode = #{0} and bshow=#{1} ")
	Currency findShowByCode(String code,boolean show);

	@Select({
			"<script>",
			"select distinct * from t_currency where ",
			"ccode in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<Currency> findByCodes(@Param("list") List<String> codes);

	@Select("select iid,ccode,csymbol from t_currency where bshow=true")
	List<Currency> getAllShowCurrencies();
	


	@Select("select * from t_currency where iid=#{0}")
	Currency getCurrencyById(Integer currencyId);

	@Select("select iid,ccode,csymbol from t_currency where iid>#{0} and bshow=true order by iid asc")
	List<Currency> getMaxCurrency(int mid);

	@Select("select max(iid) from t_currency")
	int getCurrencyByMaxId();
	
	@Insert("insert into t_currency(ccode,csymbol,fexchangerate,ccreateuser) values(#{ccode},#{csymbol},#{fexchangerate},#{ccreateuser})")
	int Insert(Currency currency);
	
	
}
