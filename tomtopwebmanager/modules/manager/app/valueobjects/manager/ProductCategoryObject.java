package valueobjects.manager;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryObject {
	private Integer id;
	private String name;
	private List<ProductCategoryObject> chileren;
	
	public ProductCategoryObject(){
		this.id=null;
		this.name="";
		this.chileren=new ArrayList<ProductCategoryObject>();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ProductCategoryObject> getChileren() {
		return chileren;
	}
	public void setChileren(List<ProductCategoryObject> chileren) {
		this.chileren = chileren;
	}
	
	
}
