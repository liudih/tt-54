package dao.dropship;

import dao.IMemberUpdateDao;
import dto.member.DropShipBase;

public interface IDropShipBaseUpdateDao extends IMemberUpdateDao {
	public int addDropShipBase(DropShipBase record);

	public int updateStatusByIid(Integer iid, Integer istatus);

	public int updateLevelByIid(Integer iid, Integer levelId);
	
	public int updateDropShip(DropShipBase dropShipBase);
}
