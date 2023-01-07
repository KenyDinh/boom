package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblUserInfo extends DaoValueInfo {
	
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "user_info";
	private static final String PRIMARY_KEY = "id";
	
	private long id;
	private String username;
	private String password;
	private String empid;
	private String name;
	private int role;
	private int dept;
	private int flag;

	public TblUserInfo() {
		this.id = 0;
		this.username = "";
		this.password = "";
		this.empid = "";
		this.name = "";
		this.role = 0;
		this.dept = 0;
		this.flag = 0;
		Sync();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getDept() {
		return dept;
	}

	public void setDept(int dept) {
		this.dept = dept;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public List<String> getSubKey() {
		return null;
	}
	
	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}
	
	public String getTableName() {
		return TABLE_NAME;
	}
	
}
