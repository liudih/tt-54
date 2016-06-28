package dto;

import java.io.Serializable;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	String title;
	int id;

	public String getTitle() {
		return title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
