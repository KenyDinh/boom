package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblBoomSeasonInfo;

public class BoomSeason {
	private TblBoomSeasonInfo boomSeasonInfo;

	public BoomSeason() {
		boomSeasonInfo = new TblBoomSeasonInfo();
	}

	public BoomSeason(TblBoomSeasonInfo boomSeasonInfo) {
		this.boomSeasonInfo = boomSeasonInfo;
	}

	public TblBoomSeasonInfo getBoomSeasonInfo() {
		return boomSeasonInfo;
	}

	public long getId() {
		return (Long) boomSeasonInfo.Get("id");
	}

	public void setId(long id) {
		boomSeasonInfo.Set("id", id);
	}

	public String getName() {
		return (String) boomSeasonInfo.Get("name");
	}

	public void setName(String name) {
		boomSeasonInfo.Set("name", name);
	}

	public String getStartTime() {
		return (String) boomSeasonInfo.Get("start_time");
	}

	public void setStartTime(String startTime) {
		boomSeasonInfo.Set("start_time", startTime);
	}

	public Date getStartTimeDate() {
		String strStartTime = getStartTime();
		if (strStartTime == null) {
			return null;
		}
		return CommonMethod.getDate(strStartTime, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getEndTime() {
		return (String) boomSeasonInfo.Get("end_time");
	}

	public void setEndTime(String endTime) {
		boomSeasonInfo.Set("end_time", endTime);
	}

	public Date getEndTimeDate() {
		String strEndTime = getEndTime();
		if (strEndTime == null) {
			return null;
		}
		return CommonMethod.getDate(strEndTime, CommonDefine.DATE_FORMAT_PATTERN);
	}

}

