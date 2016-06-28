package com.tomtop.product.models.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "list")
public class ListBean {

	public ListBean() {
	}

	public ListBean(List<?> list) {
		this.list = list;
	}

	private List<?> list;

	@XmlElements({ @XmlElement(name = "user", type = UserInfo.class),
			@XmlElement(name = "hobby", type = String.class)
	// 如果还有其它类型，继续添加
	})
	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}
