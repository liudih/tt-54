package mapper.shipping;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.shipping.ShippingStorage;

public interface ShippingStorageMapper {

	@Select({
			"<script>select count(1) as icount,istorageid,0 as idefaultstorage,0 as ioverseas from  t_product_storage_map where clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"group by istorageid order by count(1) desc</script>" })
	List<ShippingStorage> getShoppingStorageForListings(
			@Param("list") List<String> listingids);

	/**
	 * 获取listingids中是相同仓库的listing
	 * 
	 * @author lijun
	 * @param listingids
	 * @return
	 */
	@Select({
			"<script>select clistingid from  t_product_storage_map where clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"and istorageid=${storageId}</script>" })
	List<String> getSameStorageListings(@Param("list") List<String> listingids,@Param("storageId") String storageId);
}
