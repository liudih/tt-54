package valuesobject.mobile.member;

import java.io.Serializable;

public class MobileContext implements Serializable {

	private static final long serialVersionUID = 1L;

	private int iplatform;

	private String csysversion;

	private String cimei;

	private String cphonename;

	private int iappid;

	private String ccid;

	private int ilanguageid;

	private String cnetwork;

	private int currentversion;

	private String uuid;

	private String token;

	private String ip;

	private String host;

	private int icountryid;

	private int icurrencyid;

	private int website;

	public int getIcountryid() {
		return icountryid;
	}

	public void setIcountryid(int icountryid) {
		this.icountryid = icountryid;
	}

	public int getIcurrencyid() {
		return icurrencyid;
	}

	public void setIcurrencyid(int icurrencyid) {
		this.icurrencyid = icurrencyid;
	}

	public int getIplatform() {
		return iplatform;
	}

	public void setIplatform(int iplatform) {
		this.iplatform = iplatform;
	}

	public String getCsysversion() {
		return csysversion;
	}

	public void setCsysversion(String csysversion) {
		this.csysversion = csysversion;
	}

	public String getCimei() {
		return cimei;
	}

	public void setCimei(String cimei) {
		this.cimei = cimei;
	}

	public String getCphonename() {
		return cphonename;
	}

	public void setCphonename(String cphonename) {
		this.cphonename = cphonename;
	}

	public int getIappid() {
		return iappid;
	}

	public void setIappid(int iappid) {
		this.iappid = iappid;
	}

	public String getCcid() {
		return ccid;
	}

	public void setCcid(String ccid) {
		this.ccid = ccid;
	}

	public int getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(int ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCnetwork() {
		return cnetwork;
	}

	public void setCnetwork(String cnetwork) {
		this.cnetwork = cnetwork;
	}

	public int getCurrentversion() {
		return currentversion;
	}

	public void setCurrentversion(int currentversion) {
		this.currentversion = currentversion;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getWebsite() {
		return website;
	}

	public void setWebsite(int website) {
		this.website = website;
	}

}
