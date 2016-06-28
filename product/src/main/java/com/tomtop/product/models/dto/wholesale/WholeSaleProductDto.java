package com.tomtop.product.models.dto.wholesale;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.tomtop.product.utils.DateFormatUtils;

public class WholeSaleProductDto implements Serializable {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private Integer iid;

	    private Integer iwebsiteid;

	    private String cemail;

	    private String csku;

	    private Integer iqty;

	    private Date dcreatedate;

		public Integer getIid() {
			return iid;
		}

		public void setIid(Integer iid) {
			this.iid = iid;
		}

		public Integer getIwebsiteid() {
			return iwebsiteid;
		}

		public void setIwebsiteid(Integer iwebsiteid) {
			this.iwebsiteid = iwebsiteid;
		}

		public String getCemail() {
			return cemail;
		}

		public void setCemail(String cemail) {
			this.cemail = cemail;
		}

		public String getCsku() {
			return csku;
		}

		public void setCsku(String csku) {
			this.csku = csku;
		}

		public Integer getIqty() {
			return iqty;
		}

		public void setIqty(Integer iqty) {
			this.iqty = iqty;
		}

		public Date getDcreatedate() {
			if(dcreatedate == null){
				try {
					dcreatedate = DateFormatUtils.getCurrentUtcTimeDate();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			return dcreatedate;
		}

		public void setDcreatedate(Date dcreatedate) {
			this.dcreatedate = dcreatedate;
		}
}
