package dao.dropship;

import java.util.List;

import dao.IMemberEnquiryDao;
import dto.member.DropShipBase;

public interface IDropShipBaseEnquiryDao extends IMemberEnquiryDao {
	public List<DropShipBase> getDropShipBases(Integer status,
			Integer pageSize, Integer pageNum, String email, Integer level);

	public Integer getDropShipBasesCount(Integer status, String email,
			Integer level);

	public DropShipBase getDropShipBaseByEmail(String email, Integer websiteId);

	public DropShipBase getDropShipBaseByIid(Integer iid);

}
