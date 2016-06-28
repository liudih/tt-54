package com.tomtop.advert.models;

import java.io.Serializable;

public class AdvertItem implements Serializable{
	private static final long serialVersionUID = 1L;
	String title;
	String imgUrl;
	String url;
	String defaultShow;

    private String cbgimageurl;
    private String cbgcolor;
    private boolean bbgimgtile; //背景图是否平铺
    private boolean bhasbgimage;


	public AdvertItem(String title, String imgUrl, String url, String defaultShow) {
		super();
		this.title = title;
		this.imgUrl = imgUrl;
		this.url = url;
		this.defaultShow = defaultShow;
	}

	public String getTitle() {
		return title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * 默认显示格式  a,img
	 * @return
	 */
	public String getDefaultShow() {
		return this.defaultShow;
	}
	
	public String getCbgimageurl() {
		return cbgimageurl;
	}


	public String getCbgcolor() {
		return cbgcolor;
	}


	public boolean isBbgimgtile() {
		return bbgimgtile;
	}

	public boolean isBhasbgimage() {
		return bhasbgimage;
	}

}
