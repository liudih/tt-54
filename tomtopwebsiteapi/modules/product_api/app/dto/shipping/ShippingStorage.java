package dto.shipping;

import java.io.Serializable;

public class ShippingStorage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer icount;
	private Integer istorageid;
	private Integer idefaultstorage;
	private Integer ioverseas;

	public Integer getIcount() {
		return icount;
	}

	public void setIcount(Integer icount) {
		this.icount = icount;
	}

	public Integer getIstorageid() {
		return istorageid==null?0:istorageid;
	}

	public void setIstorageid(Integer istorageid) {
		this.istorageid = istorageid;
	}

	public Integer getIdefaultstorage() {
		return idefaultstorage==null?0:idefaultstorage;
	}

	public void setIdefaultstorage(Integer idefaultstorage) {
		this.idefaultstorage = idefaultstorage;
	}

	public Integer getIoverseas() {
		return ioverseas==null?0:ioverseas;
	}

	public void setIoverseas(Integer ioverseas) {
		this.ioverseas = ioverseas;
	}
	
}
