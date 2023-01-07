package dev.boom.core;

import java.io.Serializable;

public class SurveySession implements Serializable {

	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private long timeout;

	public SurveySession(String code, String name, long timeout) {
		super();
		this.code = code;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public boolean isExpired() {
		return (timeout < System.currentTimeMillis());
	}
}
