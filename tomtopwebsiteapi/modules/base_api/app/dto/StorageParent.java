package dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 仓库总表信息
 * 
 * @author Administrator
 *
 */
public class StorageParent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer iid;

	String cstoragename;

	String ccreateuser;

	Date dcreatedate;

	/**
	 * 默认仓库：仓库ID，对应多个地区
	 */
	public List<StorageDefault> storageDefaultInfo;
	/**
	 * 仓库可送达地区：仓库ID，对应多个地区
	 */
	public List<StorageArrival> storageArrivalInfo;
	/**
	 * 仓库父级对应子集：总仓库ID，对应子仓库信息
	 */
	public List<Storage> storageSubInfo;

	public List<Storage> getStorageSubInfo() {
		return storageSubInfo;
	}

	public void setStorageSubInfo(List<Storage> storageSubInfo) {
		this.storageSubInfo = storageSubInfo;
	}

	public List<StorageDefault> getStorageDefaultInfo() {
		return storageDefaultInfo;
	}

	public void setStorageDefaultInfo(List<StorageDefault> storageDefaultInfo) {
		this.storageDefaultInfo = storageDefaultInfo;
	}

	public List<StorageArrival> getStorageArrivalInfo() {
		return storageArrivalInfo;
	}

	public void setStorageArrivalInfo(List<StorageArrival> storageArrivalInfo) {
		this.storageArrivalInfo = storageArrivalInfo;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCstoragename() {
		return cstoragename;
	}

	public void setCstoragename(String cstoragename) {
		this.cstoragename = cstoragename;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}