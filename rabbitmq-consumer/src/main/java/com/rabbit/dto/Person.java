package com.rabbit.dto;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6980452227346495217L;
	private String userName;
	private String pwd;
	private int age;
	private List<String> list;
	
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Person [userName=" + userName + ", pwd=" + pwd + ", age=" + age
				+ ", list=" + list + "]";
	}
}
