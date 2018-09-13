package dev.boom.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.tbl.info.TblSurveyResultInfo;

public class SurveyResultInfo {

	private TblSurveyResultInfo info = null;

	public SurveyResultInfo(TblSurveyResultInfo info) {
		this.info = info;
	}

	public SurveyResultInfo() {
		this.info = new TblSurveyResultInfo();
	}

	public TblSurveyResultInfo getInfo() {
		return info;
	}
	
	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public long getSurveyId() {
		return this.info.getSurvey_id();
	}

	public void setSurveyId(long survey_id) {
		this.info.setSurvey_id(survey_id);
	}

	public String getUser() {
		return this.info.getUser();
	}

	public void setUser(String user) {
		this.info.setUser(user);
	}

	public String getResult() {
		return this.info.getResult();
	}
	
	public List<Integer> getResultList() {
		List<Integer> ret = null;
		String results = getResult();
		if (results != null && !results.isEmpty()) {
			for (String result : results.split(",")) {
				if (CommonMethod.isValidNumeric(result, 0, Byte.MAX_VALUE)) {
					if (ret == null) {
						ret = new ArrayList<>();
					}
					ret.add(Integer.parseInt(result));
				} else {
					GameLog.getInstance().error("[getResultArray] survey's result is invalid!");
				}
			}
		}
		return ret;
	}

	public void setResult(String result) {
		this.info.setResult(result);
	}
	
	public void setResultList(List<Integer> resuls) {
		String ret = "";
		if (resuls != null && !resuls.isEmpty()) {
			for (Integer i : resuls) {
				if (ret.length() > 0) {
					ret += ",";
				}
				ret += i.intValue();
			}
		}
	}
	
	public byte getRetryRemain() {
		return this.info.getRetry_remain();
	}

	public void setRetryRemain(byte retry_remain) {
		this.info.setRetry_remain(retry_remain);
	}

	public Date getCreated() {
		return this.info.getCreated();
	}

	public void setCreated(Date created) {
		this.info.setCreated(created);
	}
	
}
