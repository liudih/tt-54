package dto;

import java.io.Serializable;

public class MemberLevel implements Serializable  {
	private static final long serialVersionUID = 1L;
    
    private String groupname;
	
	private String nextgoupname;
	
	private double amount;
	
	private double withnextamout;

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getNextgoupname() {
		return nextgoupname;
	}

	public void setNextgoupname(String nextgoupname) {
		this.nextgoupname = nextgoupname;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getWithnextamout() {
		return withnextamout;
	}

	public void setWithnextamout(double withnextamout) {
		this.withnextamout = withnextamout;
	}
	
	
	
}
