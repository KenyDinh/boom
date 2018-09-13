package dev.boom.core;

import java.io.Serializable;

public class SurveySession implements Serializable {

	private static final long serialVersionUID = 1L;
	private String code;
	private long timeout;

	public SurveySession(String code, long timeout) {
		super();
		this.code = code;
		this.timeout = timeout;
	}

	public SurveySession() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public boolean isExpired(long timeNow) {
		return (timeout < timeNow);
	}
}
