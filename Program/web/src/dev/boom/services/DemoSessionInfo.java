package dev.boom.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.tbl.info.TblDemoSessionInfo;

public class DemoSessionInfo implements Comparable<DemoSessionInfo> {

	private TblDemoSessionInfo info = null;

	public DemoSessionInfo(TblDemoSessionInfo info) {
		this.info = info;
	}

	public DemoSessionInfo() {
		this.info = new TblDemoSessionInfo();
	}

	public TblDemoSessionInfo TblDemoSessionInfo() {
		return info;
	}

	public int getId() {
		return this.info.getId();
	}

	public Date getDemoTime() {
		return this.info.getDemo_time();
	}

	public String getDemoLocation() {
		return this.info.getDemo_location();
	}
	
	public String getContent() {
		return this.info.getContent();
	}
	
	public String getFormattedDemoTime() {
		return new SimpleDateFormat(CommonDefine.GAME_DEMO_DATE_FORMAT).format(getDemoTime());
	}
	
	public String getFormattedForJSDemoTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getDemoTime()).replace(' ', 'T');
	}
	
	public void updateValue(String demoLocation, Date demoTime, String demoContent) {
		this.info.setDemo_location(demoLocation);
		this.info.setDemo_time(demoTime);
		this.info.setContent(demoContent);
	}

	@Override
	public int compareTo(DemoSessionInfo arg0) {
		if (this.getDemoTime().before(arg0.getDemoTime())) {
			return -1;
		} else if (this.getDemoTime().after(arg0.getDemoTime())) {
			return 1;
		}
		return 0;
	}
	
	public Date getDemoFinishTime() {
		Date start = getDemoTime();
		return new Date(start.getTime() + DemoSessionService.GAME_DEMO_DURATION_DEFAULT);
	}
	
	public boolean isInSession() {
		Date start = getDemoTime();
		Date finish = new Date(start.getTime() + DemoSessionService.GAME_DEMO_DURATION_DEFAULT);
		Date now = new Date();
		return (start.before(now) && finish.after(now));
	}

}
