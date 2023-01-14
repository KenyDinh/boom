package dev.boom.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblDemoSessionInfo;

public class DemoSession implements Comparable<DemoSession> {
	private TblDemoSessionInfo demoSessionInfo;

	public DemoSession() {
		demoSessionInfo = new TblDemoSessionInfo();
	}

	public DemoSession(TblDemoSessionInfo demoSessionInfo) {
		this.demoSessionInfo = demoSessionInfo;
	}

	public TblDemoSessionInfo getDemoSessionInfo() {
		return demoSessionInfo;
	}

	public int getId() {
		return (Integer) demoSessionInfo.Get("id");
	}

	public void setId(int id) {
		demoSessionInfo.Set("id", id);
	}

	public String getDemoTime() {
		return (String) demoSessionInfo.Get("demo_time");
	}

	public void setDemoTime(String demoTime) {
		demoSessionInfo.Set("demo_time", demoTime);
	}

	public Date getDemoTimeDate() {
		String strDemoTime = getDemoTime();
		if (strDemoTime == null) {
			return null;
		}
		return CommonMethod.getDate(strDemoTime, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getDemoLocation() {
		return (String) demoSessionInfo.Get("demo_location");
	}

	public void setDemoLocation(String demoLocation) {
		demoSessionInfo.Set("demo_location", demoLocation);
	}

	public String getContent() {
		return (String) demoSessionInfo.Get("content");
	}

	public void setContent(String content) {
		demoSessionInfo.Set("content", content);
	}
	
	public String getFormattedDemoTime() {
		return new SimpleDateFormat(CommonDefine.GAME_DEMO_DATE_FORMAT).format(getDemoTime());
	}
	
	public String getFormattedForJSDemoTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getDemoTime()).replace(' ', 'T');
	}
	
	public void updateValue(String demoLocation, Date demoTime, String demoContent) {
		this.demoSessionInfo.Set("demo_location", demoLocation);
		this.demoSessionInfo.Set("demo_time", demoTime);
		this.demoSessionInfo.Set("content", demoContent);
	}

	@Override
	public int compareTo(DemoSession arg0) {
		if (this.getDemoTimeDate().before(arg0.getDemoTimeDate())) {
			return -1;
		} else if (this.getDemoTimeDate().after(arg0.getDemoTimeDate())) {
			return 1;
		}
		return 0;
	}
	
	public Date getDemoFinishTime() {
		Date start = getDemoTimeDate();
		return new Date(start.getTime() + DemoSessionService.GAME_DEMO_DURATION_DEFAULT);
	}
	
	public boolean isInSession() {
		Date start = getDemoTimeDate();
		Date finish = new Date(start.getTime() + DemoSessionService.GAME_DEMO_DURATION_DEFAULT);
		Date now = new Date();
		return (start.before(now) && finish.after(now));
	}

}

