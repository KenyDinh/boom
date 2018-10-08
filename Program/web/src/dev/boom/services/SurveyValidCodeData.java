package dev.boom.services;

import dev.boom.tbl.data.TblSurveyValidCodeData;

public class SurveyValidCodeData {
	
	private TblSurveyValidCodeData data = null;
	
	public SurveyValidCodeData() {
		this.data = new TblSurveyValidCodeData();
	}
	
	public SurveyValidCodeData(TblSurveyValidCodeData data) {
		this.data = data;
	}
	
	public TblSurveyValidCodeData getTblData() {
		return this.data;
	}
	
	public int getId() {
		return this.data.getId();
	}

	public String getCode() {
		return this.data.getCode();
	}

	public String getName() {
		return this.data.getName();
	}

	public String getEmployeeId() {
		return this.data.getEmp_id();
	}
	
	public int getFlag() {
		return this.data.getFlag();
	}
	
	public boolean isEditable() {
		return (getFlag() == 1);
	}
	
	public boolean isReadonly() {
		return (getFlag() == 2);
	}
}
