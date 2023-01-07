package dev.boom.services.json;

public class SurveyResultObject {

	public String user_id;
	public String username;
	public String deparment;
	public String result;

	public SurveyResultObject() {
	}

	public SurveyResultObject(String user_id, String username, String deparment, String result) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.deparment = deparment;
		this.result = result;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeparment() {
		return deparment;
	}

	public void setDeparment(String deparment) {
		this.deparment = deparment;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
