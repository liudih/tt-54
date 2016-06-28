package dao.dropship;

import java.util.List;

import dao.IMemberEnquiryDao;
import dto.member.DropShipLevel;

public interface IDropShipLevelEnquiryDao extends IMemberEnquiryDao {
	public List<DropShipLevel> getDropShipLevels();

	public DropShipLevel getDropShipLevelById(Integer levelid);

	public DropShipLevel getByTotalPrice(double total);
}
