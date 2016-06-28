package com.tomtop.search.entiry;

import java.io.Serializable;

/**
 * 标签实体对象
 * @author ztiny
 *
 */
public class TagEntity  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2827959727074627030L;
	/**主键ID*/
	private int id;
	/**标签名称*/
	private String tagName;
	/**发布时间*/
	private String releaseTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	
	
}
